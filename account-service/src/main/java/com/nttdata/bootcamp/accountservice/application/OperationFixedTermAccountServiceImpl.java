package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.CurrentAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.Transaction;
import com.nttdata.bootcamp.accountservice.model.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.TypeTransaction;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class OperationFixedTermAccountServiceImpl implements OperationService{
    @Autowired
    ProductClient productClient;
    @Autowired
    FixedTermAccountRepository fixedTermAccountRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return fixedTermAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(fixedTermAccount -> {
            if(fixedTermAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermAccount.getProductId()).flatMap(productAccountDto-> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermAccount.getId()).flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermAccount.setUpdatedAt(new Date());
                    fixedTermAccount.setBalance(fixedTermAccount.getBalance().add(depositDto.getAmount()));
                    depositDto.setAccountId(fixedTermAccount.getId());
                    depositDto.setDestinationId(fixedTermAccount.getId());
                    return fixedTermAccountRepository.save(fixedTermAccount)
                            .flatMap(ft -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE))
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return fixedTermAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber()).flatMap(fixedTermAccount -> {
            if(fixedTermAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            if (withdrawDto.getAmount().compareTo(fixedTermAccount.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
            }
            return productClient.getProductAccount(fixedTermAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.countByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermAccount.getId()).flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermAccount.setUpdatedAt(new Date());
                    fixedTermAccount.setBalance(fixedTermAccount.getBalance().subtract(withdrawDto.getAmount()));
                    withdrawDto.setAccountId(fixedTermAccount.getId());
                    withdrawDto.setOriginId(fixedTermAccount.getId());
                    return fixedTermAccountRepository.save(fixedTermAccount)
                            .flatMap(ft-> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT))
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> wireTransfer(OperationDto withdrawDto) {
        return null;
    }
    public Mono<Transaction> saveTransaction(OperationDto withdrawDto, TypeTransaction typeTxn, TypeAffectation typeAffectation){
        Transaction transaction = new Transaction();
        transaction.setAccountId(withdrawDto.getAccountId());
        transaction.setOrigin(withdrawDto.getOriginId());
        transaction.setDestination(withdrawDto.getDestinationId());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(withdrawDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(withdrawDto.getOperation());
        transaction.setAffectation(typeAffectation);
        transaction.setTypeAccount(TypeAccount.FIXED_TERM_ACCOUNT);
        return transactionRepository.insert(transaction);
    }
}
