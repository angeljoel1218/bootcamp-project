package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.OperationAccountException;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.*;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.ProductClientService;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.constant.TypeTransaction;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import com.nttdata.bootcamp.accountservice.model.dto.SavingsAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SavingsAccountOperationServiceImpl implements OperationService<SavingsAccountDto>{
    @Autowired
    ProductClientService productClient;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransferBalanceService transferBalanceService;
    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return savingsAccountRepository.findByNumberAndTypeAccount(depositDto.getDestAccountNumber(), TypeAccount.SAVINGS_ACCOUNT).flatMap(savingsAccount -> {
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).flatMap(count -> {
                    BigDecimal commission = BigDecimal.ZERO;
                    if(productAccountDto.getMaxMovements() <= count) {
                        commission = commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance().add(depositDto.getAmount()).subtract(commission));
                    savingsAccount.setUpdatedAt(new Date());
                    depositDto.setAccountId(savingsAccount.getId());
                    depositDto.setAmount(depositDto.getAmount().subtract(commission));
                    depositDto.setCommission(commission);
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE, TypeAccount.SAVINGS_ACCOUNT))
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new OperationAccountException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return savingsAccountRepository.findByNumberAndTypeAccount(withdrawDto.getOrigAccountNumber(),TypeAccount.SAVINGS_ACCOUNT).flatMap(savingsAccount -> {
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).flatMap(count-> {
                    BigDecimal commission = BigDecimal.ZERO;
                    if(productAccountDto.getMaxMovements() <= count) {
                        commission = commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                    }
                    BigDecimal totalAmount = withdrawDto.getAmount().add(commission);
                    if (totalAmount.compareTo(savingsAccount.getBalance()) == 1) {
                        return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance().subtract(totalAmount));
                    savingsAccount.setUpdatedAt(new Date());
                    withdrawDto.setAccountId(savingsAccount.getId());
                    withdrawDto.setAmount(withdrawDto.getAmount().add(commission));
                    withdrawDto.setCommission(commission);
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT, TypeAccount.SAVINGS_ACCOUNT))
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new OperationAccountException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    @Override
    public Mono<String> wireTransfer(OperationDto operationDto) {
        return savingsAccountRepository.findByNumberAndTypeAccount(operationDto.getOrigAccountNumber(),TypeAccount.SAVINGS_ACCOUNT).flatMap(savingsAccount -> {
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).flatMap(count-> {
                    BigDecimal commission = BigDecimal.ZERO;
                    if(productAccountDto.getMaxMovements() <= count) {
                        commission = commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                    }
                    BigDecimal totalAmount = operationDto.getAmount().add(commission);
                    if (totalAmount.compareTo(savingsAccount.getBalance()) == 1) {
                        return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                    }
                    BigDecimal finalCommission = commission;

                    return this.transferBalanceService.saveTransferIn(operationDto).flatMap(operationDto1 -> {
                        savingsAccount.setBalance(savingsAccount.getBalance().subtract(totalAmount));
                        savingsAccount.setUpdatedAt(new Date());
                        operationDto.setAccountId(savingsAccount.getId());
                        operationDto.setAmount(operationDto.getAmount().add(finalCommission));
                        operationDto.setCommission(finalCommission);
                        return savingsAccountRepository.save(savingsAccount)
                                .flatMap(rs -> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.DECREMENT, TypeAccount.SAVINGS_ACCOUNT))
                                .then(Mono.just("Transfer completed successfully"));
                    });
                });
            }).switchIfEmpty(Mono.error(new OperationAccountException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    public Mono<Transaction> saveTransaction(OperationDto operationDto, TypeTransaction typeTxn, TypeAffectation typeAffectation, TypeAccount typeAccount){
        Transaction transaction = new Transaction();
        transaction.setAccountId(operationDto.getAccountId());
        transaction.setOrigin(operationDto.getOrigAccountNumber());
        transaction.setDestination(operationDto.getDestAccountNumber());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(operationDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(operationDto.getOperation());
        transaction.setAffectation(typeAffectation);
        transaction.setTypeAccount(typeAccount);
        transaction.setCommission(operationDto.getCommission());
        return transactionRepository.insert(transaction);
    }
}
