package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperFixedTermDeposit;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.client.ProductClient;
import com.nttdata.bootcamp.accountservice.client.UserClient;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermDepositAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public class FixedTermDepositAccountServiceImpl implements AccountService<FixedTermDepositAccountDto> {
    @Autowired
    FixedTermDepositAccountRepository fixedTermDepositAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserClient userClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    MapperFixedTermDeposit mapperFixedTermDeposit;
    @Autowired
    MapperTransaction mapperTransaction;
    @Override
    public Mono<FixedTermDepositAccountDto> create(FixedTermDepositAccountDto accountDto) {
        return userClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getIdType().equals(TypeClient.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Client must be personal type"));
            }
            return fixedTermDepositAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(fixedTermDepositAccount -> {
                if (fixedTermDepositAccount.getId() != null) {
                    return Mono.error(new IllegalArgumentException("The client can only have one fixed term deposit account"));
                }
                return productClient.getProductAccountByType(TypeAccount.FIXED_TERM).flatMap(productAccountDto -> {
                    accountDto.setProductId(productAccountDto.getId());
                    accountDto.setState(StateAccount.ACTIVE);
                    accountDto.setCreatedAt(new Date());
                    Mono<FixedTermDepositAccount> fixedTermDepositAccountMono = mapperFixedTermDeposit.toProductAccount(accountDto)
                            .flatMap(fixedTermDepositAccountRepository::insert);
                    return fixedTermDepositAccountMono
                            .flatMap(mapperFixedTermDeposit::toDto);
                });
            });
        });
    }

    @Override
    public Mono<FixedTermDepositAccountDto> findByHolderId(String holderId) {
        return fixedTermDepositAccountRepository.findByHolderId(holderId)
                .flatMap(mapperFixedTermDeposit::toDto);
    }

    @Override
    public Mono<FixedTermDepositAccountDto> findByNumber(String number) {
        return fixedTermDepositAccountRepository.findByNumber(number)
                .flatMap(mapperFixedTermDeposit::toDto);
    }

    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return fixedTermDepositAccountRepository.findByNumber(accountId)
                .flatMap(fixedTermDepositAccountRepository::delete);
    }

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return fixedTermDepositAccountRepository.findByNumber(depositDto.getAccountNumber()).flatMap(fixedTermDepositAccount -> {
            if(fixedTermDepositAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermDepositAccount.getProductId()).flatMap(productAccountDto-> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), depositDto.getAccountNumber()).count().flatMap(tx -> {
                    if(productAccountDto.getMaxMovements() <= tx) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermDepositAccount.setUpdatedAt(new Date());
                    fixedTermDepositAccount.setBalance(fixedTermDepositAccount.getBalance() + depositDto.getAmount());
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(fixedTermDepositAccount.getId());
                    transaction.setDateOfTransaction(new Date());
                    transaction.setAmount(depositDto.getAmount());
                    transaction.setType(TypeTransaction.INCOMING);
                    transaction.setOperation(depositDto.getOperation());
                    return fixedTermDepositAccountRepository.save(fixedTermDepositAccount)
                            .then(Mono.just(transactionRepository.insert(transaction)).then(Mono.just("Deposit completed successfully")));
                });
            });
        });
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return fixedTermDepositAccountRepository.findByNumber(withdrawDto.getAccountNumber()).flatMap(fixedTermDepositAccount -> {
            if(fixedTermDepositAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermDepositAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), withdrawDto.getAccountNumber()).count().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermDepositAccount.setUpdatedAt(new Date());
                    fixedTermDepositAccount.setBalance(fixedTermDepositAccount.getBalance() - withdrawDto.getAmount());
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(fixedTermDepositAccount.getId());
                    transaction.setDateOfTransaction(new Date());
                    transaction.setAmount(withdrawDto.getAmount());
                    transaction.setType(TypeTransaction.INCOMING);
                    transaction.setOperation(withdrawDto.getOperation());
                    return fixedTermDepositAccountRepository.save(fixedTermDepositAccount)
                            .then(Mono.just(transactionRepository.insert(transaction)).then(Mono.just("Withdrawal completed successfully")));
                });
            });
        });
    }
}
