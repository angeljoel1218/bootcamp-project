package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperSavingsAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.client.ProductClient;
import com.nttdata.bootcamp.accountservice.client.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class SavingsAccountServiceImpl implements AccountService<SavingsAccountDto> {
    @Autowired
    MapperSavingsAccount mapperSavingsAccount;

    @Autowired
    MapperTransaction mapperTransaction;

    @Autowired
    ProductClient productClient;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<SavingsAccountDto> create(SavingsAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getIdType().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Client must be personal type"));
            }
            return savingsAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(savingsAccount -> {
                if (savingsAccount.getId() != null) {
                    return Mono.error(new IllegalArgumentException("The client can only have one savings account"));
                }
                return this.save(accountDto);
            }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }

    public Mono<SavingsAccountDto> save(SavingsAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            Mono<SavingsAccount> savingsAccountMono = mapperSavingsAccount.toSavingsAccount(accountDto)
                    .flatMap(savingsAccountRepository::insert);

            return savingsAccountMono.flatMap(mapperSavingsAccount::toDto);
        });
    };

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
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).collectList().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance() + depositDto.getAmount());
                    savingsAccount.setUpdatedAt(new Date());
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> {
                                Transaction transaction = new Transaction();
                                transaction.setAccountId(savingsAccount.getId());
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
        return savingsAccountRepository.findByNumber(withdrawDto.getAccountNumber()).flatMap(savingsAccount -> {
            if (savingsAccount.getBalance() < withdrawDto.getAmount()) {
                return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
            }
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).collectList().flatMap(count-> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance() - withdrawDto.getAmount());
                    savingsAccount.setUpdatedAt(new Date());
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> {
                                Transaction transaction = new Transaction();
                                transaction.setAccountId(savingsAccount.getId());
                                transaction.setDateOfTransaction(new Date());
                                transaction.setAmount(withdrawDto.getAmount());
                                transaction.setType(TypeTransaction.OUTGOING);
                                transaction.setOperation(withdrawDto.getOperation());
                                return transactionRepository.insert(transaction);
                            })
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }
}
