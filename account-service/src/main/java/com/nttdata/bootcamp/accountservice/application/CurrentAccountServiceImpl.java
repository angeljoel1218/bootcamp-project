package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperCurrentAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.client.ProductClient;
import com.nttdata.bootcamp.accountservice.client.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.CurrentAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CurrentAccountServiceImpl implements AccountService<CurrentAccountDto> {
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    MapperCurrentAccount mapperCurrentAccount;
    @Autowired
    MapperTransaction mapperTransaction;
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public Mono<CurrentAccountDto> create(CurrentAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return currentAccountRepository.findByHolderId(accountDto.getHolderId())
                .flatMap(aa -> customerClient.getClient(accountDto.getHolderId()).flatMap(cl -> {
                    if(aa.getId() != null && cl.getIdType().equals(TypeCustomer.PERSONAL)) {
                        return Mono.error(new IllegalArgumentException("The client can only have one savings account"));
                    }
                    return this.save(accountDto);
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found"))))
                .switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
    }

    public Mono<CurrentAccountDto> save(CurrentAccountDto accountDto) {
        return productClient.getProductAccountByType(TypeAccount.CURRENT_ACCOUNT).flatMap(pr -> {
            accountDto.setProductId(pr.getId());
            return mapperCurrentAccount.toCurrentAccount(accountDto)
                    .flatMap(currentAccountRepository::insert)
                    .flatMap(mapperCurrentAccount::toDto);
        });
    };

    @Override
    public Mono<CurrentAccountDto> findByHolderId(String holderId) {
        return currentAccountRepository.findByHolderId(holderId)
                .flatMap(mapperCurrentAccount::toDto);
    }

    @Override
    public Mono<CurrentAccountDto> findByNumber(String number) {
        return currentAccountRepository.findByNumber(number)
                .flatMap(mapperCurrentAccount::toDto);
    }

    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return currentAccountRepository.findByHolderId(accountId)
                .flatMap(currentAccountRepository::delete);
    }

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return currentAccountRepository.findByNumber(depositDto.getAccountNumber()).flatMap(currentAccount -> {
            currentAccount.setBalance(currentAccount.getBalance() + depositDto.getAmount());
            currentAccount.setUpdatedAt(new Date());
            return currentAccountRepository.save(currentAccount)
                    .flatMap(ca -> {
                        Transaction transaction = new Transaction();
                        transaction.setAccountId(currentAccount.getId());
                        transaction.setDateOfTransaction(new Date());
                        transaction.setAmount(depositDto.getAmount());
                        transaction.setType(TypeTransaction.INCOMING);
                        transaction.setOperation(depositDto.getOperation());
                        return transactionRepository.insert(transaction);
                    }).then(Mono.just("Deposit completed successfully"));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return currentAccountRepository.findByNumber(withdrawDto.getAccountNumber())
                .flatMap(currentAccount -> {
                    if (currentAccount.getBalance() < withdrawDto.getAmount()) {
                        return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
                    }
                    currentAccount.setBalance(currentAccount.getBalance() - withdrawDto.getAmount());
                    currentAccount.setUpdatedAt(new Date());
                    return currentAccountRepository.save(currentAccount)
                            .flatMap(ca -> {
                                Transaction transaction = new Transaction();
                                transaction.setAccountId(currentAccount.getId());
                                transaction.setDateOfTransaction(new Date());
                                transaction.setAmount(withdrawDto.getAmount());
                                transaction.setType(TypeTransaction.OUTGOING);
                                transaction.setOperation(withdrawDto.getOperation());
                                return transactionRepository.insert(transaction);
                            }).then(Mono.just("Withdrawal completed successfully"));
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }
}
