package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperFixedTermDeposit;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermDepositAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class FixedTermDepositAccountServiceImpl implements AccountService<FixedTermDepositAccountDto> {
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    FixedTermDepositAccountRepository fixedTermDepositAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MapperFixedTermDeposit mapperFixedTermDeposit;
    @Autowired
    MapperTransaction mapperTransaction;
    @Override
    public Mono<FixedTermDepositAccountDto> create(FixedTermDepositAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getIdType().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Customer must be personal type"));
            }
            return fixedTermDepositAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(fixedTermDepositAccount -> {
                if (fixedTermDepositAccount.getId() != null) {
                    return Mono.error(new IllegalArgumentException("The customer can only have one fixed term deposit account"));
                }
                return this.save(accountDto);
            }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }

    public Mono<FixedTermDepositAccountDto> save(FixedTermDepositAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            if (productAccountDto.getMinFixedAmount() <= accountDto.getBalance()) {
                return Mono.error(new IllegalArgumentException("Insufficient minimum amount to open an account"));
            }
            Mono<FixedTermDepositAccount> fixedTermDepositAccountMono = mapperFixedTermDeposit.toProductAccount(accountDto)
                    .flatMap(fixedTermDepositAccountRepository::insert);
            return fixedTermDepositAccountMono
                    .flatMap(mapperFixedTermDeposit::toDto);
        });
    };

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
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermDepositAccount.getId()).collectList().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermDepositAccount.setUpdatedAt(new Date());
                    fixedTermDepositAccount.setBalance(fixedTermDepositAccount.getBalance() + depositDto.getAmount());
                    return fixedTermDepositAccountRepository.save(fixedTermDepositAccount)
                            .flatMap(ft -> {
                                Transaction transaction = new Transaction();
                                transaction.setAccountId(fixedTermDepositAccount.getId());
                                transaction.setDateOfTransaction(new Date());
                                transaction.setAmount(depositDto.getAmount());
                                transaction.setType(TypeTransaction.INCOMING);
                                transaction.setOperation(depositDto.getOperation());
                                return transactionRepository.insert(transaction);
                            })
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return fixedTermDepositAccountRepository.findByNumber(withdrawDto.getAccountNumber()).flatMap(fixedTermDepositAccount -> {
            if(fixedTermDepositAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermDepositAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermDepositAccount.getId()).collectList().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermDepositAccount.setUpdatedAt(new Date());
                    fixedTermDepositAccount.setBalance(fixedTermDepositAccount.getBalance() - withdrawDto.getAmount());
                    return fixedTermDepositAccountRepository.save(fixedTermDepositAccount)
                            .flatMap(ft-> {
                                Transaction transaction = new Transaction();
                                transaction.setAccountId(fixedTermDepositAccount.getId());
                                transaction.setDateOfTransaction(new Date());
                                transaction.setAmount(withdrawDto.getAmount());
                                transaction.setType(TypeTransaction.INCOMING);
                                transaction.setOperation(withdrawDto.getOperation());
                                return transactionRepository.insert(transaction);
                            })
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }
}
