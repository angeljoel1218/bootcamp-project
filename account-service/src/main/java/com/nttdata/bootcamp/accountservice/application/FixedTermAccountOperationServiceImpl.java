package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.OperationAccountException;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.Transaction;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.constant.TypeTransaction;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class FixedTermAccountOperationServiceImpl implements OperationService{
    @Autowired
    ProductClient productClient;
    @Autowired
    FixedTermAccountRepository fixedTermAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransferBalanceService transferBalanceService;

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return fixedTermAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(fixedTermAccount -> {
            if(fixedTermAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new OperationAccountException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermAccount.getProductId()).flatMap(productAccountDto-> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermAccount.getId()).flatMap(count -> {
                    BigDecimal commission = BigDecimal.ZERO;
                    if(productAccountDto.getMaxMovements() <= count) {
                        commission = commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                    }
                    BigDecimal totalAmount = fixedTermAccount.getBalance().add(depositDto.getAmount()).subtract(commission);
                    fixedTermAccount.setUpdatedAt(new Date());
                    fixedTermAccount.setBalance(totalAmount);
                    depositDto.setAccountId(fixedTermAccount.getId());
                    depositDto.setDestinationId(fixedTermAccount.getId());
                    depositDto.setCommission(commission);
                    return fixedTermAccountRepository.save(fixedTermAccount)
                            .flatMap(ft -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE))
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new OperationAccountException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return fixedTermAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber()).flatMap(fixedTermAccount -> {
            if(fixedTermAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new OperationAccountException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermAccount.getId()).flatMap(count -> {
                    BigDecimal commission = BigDecimal.ZERO;
                    if(productAccountDto.getMaxMovements() <= count) {
                        commission = commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                    }
                    BigDecimal totalAmount = withdrawDto.getAmount().add(commission);
                    if (totalAmount.compareTo(fixedTermAccount.getBalance()) == 1) {
                        return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                    }
                    fixedTermAccount.setUpdatedAt(new Date());
                    fixedTermAccount.setBalance(fixedTermAccount.getBalance().subtract(totalAmount));
                    withdrawDto.setAccountId(fixedTermAccount.getId());
                    withdrawDto.setOriginId(fixedTermAccount.getId());
                    withdrawDto.setCommission(commission);
                    return fixedTermAccountRepository.save(fixedTermAccount)
                            .flatMap(ft-> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT))
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new OperationAccountException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    @Override
    public Mono<String> wireTransfer(OperationDto operationDto) {
        return fixedTermAccountRepository.findByNumber(operationDto.getOrigAccountNumber())
                .flatMap(fixedTermAccount -> {
                    if(fixedTermAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                        return Mono.error(new OperationAccountException("Date not allowed to perform operations"));
                    }
                    if (operationDto.getAmount().compareTo(fixedTermAccount.getBalance()) == 1) {
                        return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                    }
                    return productClient.getProductAccount(fixedTermAccount.getProductId()).flatMap(productAccountDto -> {
                        return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermAccount.getId()).flatMap(count -> {
                            BigDecimal commission = BigDecimal.ZERO;
                            if(productAccountDto.getMaxMovements() <= count) {
                                commission = commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                            }
                            BigDecimal totalAmount = operationDto.getAmount().add(commission);
                            if (totalAmount.compareTo(fixedTermAccount.getBalance()) == 1) {
                                return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                            }
                            BigDecimal finalCommission = commission;
                            return transferBalanceService.saveTransferOut(operationDto).flatMap(operationDto1 -> {
                                fixedTermAccount.setBalance(fixedTermAccount.getBalance().subtract(totalAmount));
                                fixedTermAccount.setUpdatedAt(new Date());
                                operationDto.setAccountId(fixedTermAccount.getId());
                                operationDto.setOriginId(fixedTermAccount.getId());
                                operationDto.setDestinationId(operationDto1.getDestinationId());
                                operationDto.setCommission(finalCommission);
                                return fixedTermAccountRepository.save(fixedTermAccount)
                                        .flatMap(ft-> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.DECREMENT))
                                        .then(Mono.just("Transfer completed successfully"));
                            });
                        });
                    }).switchIfEmpty(Mono.error(new OperationAccountException("Product does not exist")));
                }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    public Mono<Transaction> saveTransaction(OperationDto operationDto, TypeTransaction typeTxn, TypeAffectation typeAffectation){
        Transaction transaction = new Transaction();
        transaction.setAccountId(operationDto.getAccountId());
        transaction.setOrigin(operationDto.getOriginId());
        transaction.setDestination(operationDto.getDestinationId());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(operationDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(operationDto.getOperation());
        transaction.setAffectation(typeAffectation);
        transaction.setTypeAccount(TypeAccount.FIXED_TERM_ACCOUNT);
        transaction.setCommission(operationDto.getCommission());
        return transactionRepository.insert(transaction);
    }
}
