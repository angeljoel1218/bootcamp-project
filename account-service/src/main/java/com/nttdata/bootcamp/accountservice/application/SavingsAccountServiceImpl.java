package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperSavingsAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.client.ProductClient;
import com.nttdata.bootcamp.accountservice.client.UserClient;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public class SavingsAccountServiceImpl implements AccountService<SavingsAccountDto> {
    @Autowired
    MapperSavingsAccount mapperSavingsAccount;

    @Autowired
    MapperTransaction mapperTransaction;

    @Autowired
    ProductClient productClient;

    @Autowired
    UserClient userClient;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<SavingsAccountDto> create(SavingsAccountDto accountDto) {
        return userClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getIdType().equals(TypeClient.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Client must be personal type"));
            }
            return savingsAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(savingsAccount -> {
                if (savingsAccount.getId() != null) {
                    return Mono.error(new IllegalArgumentException("The client can only have one savings account"));
                }
                return productClient.getProductAccountByType(TypeAccount.SAVING).flatMap(productAccountDto -> {
                    accountDto.setProductId(productAccountDto.getId());
                    accountDto.setState(StateAccount.ACTIVE);
                    accountDto.setCreatedAt(new Date());

                    Mono<SavingsAccount> savingsAccountMono = mapperSavingsAccount.toSavingsAccount(accountDto)
                            .flatMap(savingsAccountRepository::insert);

                    return savingsAccountMono.flatMap(mapperSavingsAccount::toDto);
                });
            });
        });
    }

    @Override
    public Mono<SavingsAccountDto> findByHolderId(String holderId) {
        return savingsAccountRepository.findByHolderId(holderId)
                .flatMap(mapperSavingsAccount::toDto);
    }
    @Override
    public Mono<SavingsAccountDto> findByNumber(String number) {
        return savingsAccountRepository.findByNumber(number)
                .flatMap(mapperSavingsAccount::toDto);
    }

    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return savingsAccountRepository.findById(accountId)
                .flatMap(savingsAccountRepository::delete);
    }

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return savingsAccountRepository.findByNumber(depositDto.getAccountNumber()).flatMap(savingsAccount -> {
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), depositDto.getAccountNumber()).count().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance() + depositDto.getAmount());
                    savingsAccount.setUpdatedAt(new Date());
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(savingsAccount.getId());
                    transaction.setDateOfTransaction(new Date());
                    transaction.setAmount(depositDto.getAmount());
                    transaction.setType(TypeTransaction.INCOMING);
                    transaction.setOperation(depositDto.getOperation());
                    return savingsAccountRepository.save(savingsAccount)
                            .then(Mono.just(transactionRepository.insert(transaction)).then(Mono.just("Deposit completed successfully")));
                });
            });
        });
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return savingsAccountRepository.findByNumber(withdrawDto.getAccountNumber()).flatMap(savingsAccount -> {
            if (savingsAccount.getBalance() < withdrawDto.getAmount()) {
                return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
            }
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), withdrawDto.getAccountNumber()).count().flatMap(count-> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance() - withdrawDto.getAmount());
                    savingsAccount.setUpdatedAt(new Date());
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(savingsAccount.getId());
                    transaction.setDateOfTransaction(new Date());
                    transaction.setAmount(withdrawDto.getAmount());
                    transaction.setType(TypeTransaction.OUTGOING);
                    transaction.setOperation(withdrawDto.getOperation());
                    return savingsAccountRepository.save(savingsAccount)
                            .then(Mono.just(transactionRepository.insert(transaction)).then(Mono.just("Withdrawal completed successfully")));
                });
            });
        });
    }
}
