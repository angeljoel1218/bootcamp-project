package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.feignclient.CreditClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.*;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OperationSavingsAccountServiceImpl implements OperationService{
    @Autowired
    ProductClient productClient;
    @Autowired
    CustomerClient customerClient;
    @Autowired
    CreditClient creditClient;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    FixedTermAccountRepository fixedTermAccountRepository;
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return savingsAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(savingsAccount -> {
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance().add(depositDto.getAmount()));
                    savingsAccount.setUpdatedAt(new Date());
                    depositDto.setAccountId(savingsAccount.getId());
                    depositDto.setDestinationId(savingsAccount.getId());
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE, TypeAccount.SAVINGS_ACCOUNT))
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return savingsAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber()).flatMap(savingsAccount -> {
            if (withdrawDto.getAmount().compareTo(savingsAccount.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
            }
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).flatMap(count-> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance().subtract(withdrawDto.getAmount()));
                    savingsAccount.setUpdatedAt(new Date());
                    withdrawDto.setAccountId(savingsAccount.getId());
                    withdrawDto.setOriginId(savingsAccount.getId());
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT, TypeAccount.SAVINGS_ACCOUNT))
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> wireTransfer(OperationDto operationDto) {
        return savingsAccountRepository.findByNumber(operationDto.getOrigAccountNumber()).flatMap(savingsAccount -> {
            if(!savingsAccount.getHolderId().equals(operationDto.getHolderId())) {
              return Mono.error(new IllegalArgumentException("Account not found"));
            }
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).flatMap(count-> {
                    BigDecimal commission = BigDecimal.ZERO;
                    if(productAccountDto.getMaxMovements() < count) {
                        commission.add(BigDecimal.valueOf(productAccountDto.getCommissionAmount()));
                    }
                    BigDecimal totalAmount = operationDto.getAmount().add(commission);
                    if (totalAmount.compareTo(savingsAccount.getBalance()) == 1) {
                        return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
                    }
                    return this.saveTransferOut(operationDto).flatMap(operationDto1 -> {
                        savingsAccount.setBalance(savingsAccount.getBalance().subtract(totalAmount));
                        savingsAccount.setUpdatedAt(new Date());
                        operationDto.setAccountId(savingsAccount.getId());
                        operationDto.setOriginId(savingsAccount.getId());
                        operationDto.setDestinationId(operationDto1.getDestinationId());
                        operationDto.setCommission(commission);
                        return savingsAccountRepository.save(savingsAccount).flatMap(rs -> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.DECREMENT, TypeAccount.SAVINGS_ACCOUNT))
                                .then(Mono.just("Transfer completed successfully"));
                    });
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    public Mono<OperationDto> saveTransferOut(OperationDto operationDto) {
        return accountRepository.findByNumber(operationDto.getDestAccountNumber()).flatMap(account -> {
            if (account.getTypeAccount().equals(TypeAccount.SAVINGS_ACCOUNT)) {
                return savingsAccountRepository.findByNumber(operationDto.getDestAccountNumber()).flatMap(savingsAccount -> {
                    operationDto.setAccountId(savingsAccount.getId());
                    operationDto.setDestinationId(savingsAccount.getId());
                    savingsAccount.setUpdatedAt(new Date());
                    savingsAccount.setBalance(savingsAccount.getBalance().add(operationDto.getAmount()));
                    return savingsAccountRepository.save(savingsAccount);
                }).flatMap(rs-> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.INCREASE, TypeAccount.SAVINGS_ACCOUNT))
                        .then(Mono.just(operationDto));
            }
            if (account.getTypeAccount().equals(TypeAccount.CURRENT_ACCOUNT)) {
                return currentAccountRepository.findByNumber(operationDto.getDestAccountNumber()).flatMap(currentAccount -> {
                    operationDto.setAccountId(currentAccount.getId());
                    operationDto.setDestinationId(currentAccount.getId());
                    currentAccount.setUpdatedAt(new Date());
                    currentAccount.setBalance(currentAccount.getBalance().add(operationDto.getAmount()));
                    return currentAccountRepository.save(currentAccount);
                }).flatMap(rs-> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.INCREASE, TypeAccount.CURRENT_ACCOUNT))
                        .then(Mono.just(operationDto));
            }
            if (account.getTypeAccount().equals(TypeAccount.FIXED_TERM_ACCOUNT)) {
               return fixedTermAccountRepository.findByNumber(operationDto.getDestAccountNumber()).flatMap(fixedTermAccount -> {
                   operationDto.setAccountId(fixedTermAccount.getId());
                   operationDto.setDestinationId(fixedTermAccount.getId());
                   fixedTermAccount.setUpdatedAt(new Date());
                   fixedTermAccount.setBalance(fixedTermAccount.getBalance().add(operationDto.getAmount()));
                   return fixedTermAccountRepository.save(fixedTermAccount);
               }).flatMap(rs-> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.INCREASE, TypeAccount.FIXED_TERM_ACCOUNT))
                       .then(Mono.just(operationDto));
            }
            return Mono.error(new IllegalArgumentException("Account destination does not exist"));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account destination does not exist")));
    }
    public Mono<Transaction> saveTransaction(OperationDto withdrawDto, TypeTransaction typeTxn, TypeAffectation typeAffectation, TypeAccount typeAccount){
        Transaction transaction = new Transaction();
        transaction.setAccountId(withdrawDto.getAccountId());
        transaction.setOrigin(withdrawDto.getOriginId());
        transaction.setDestination(withdrawDto.getDestinationId());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(withdrawDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(withdrawDto.getOperation());
        transaction.setAffectation(typeAffectation);
        transaction.setTypeAccount(typeAccount);
        return transactionRepository.insert(transaction);
    }
}
