package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperCurrentAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.feignclient.CreditClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.CurrentAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CurrentAccountServiceImpl implements AccountService<CurrentAccountDto> {
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    CreditClient creditClient;
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
        return customerClient.getClient(accountDto.getHolderId()).flatMap(customerDto -> {
            if(customerDto.getTypeProfile().equals(TypeProfile.PYME)) {
                return creditClient.getCreditCardCustomer(accountDto.getHolderId())
                        .flatMap(creditCardDto -> {
                            if (creditCardDto.getId() != null) {
                                return this.findAndSave(accountDto, customerDto);
                            }
                            return Mono.error(new IllegalArgumentException("The customer must have a credit card to enable this account"));
                        }).switchIfEmpty(Mono.error(new IllegalArgumentException("The customer must have a credit card to enable this account")));
            }
            return this.findAndSave(accountDto, customerDto);
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }

    public Mono<CurrentAccountDto> findAndSave(CurrentAccountDto accountDto, CustomerDto customerDto) {
        return currentAccountRepository.findByHolderId(accountDto.getHolderId())
                .flatMap(aa -> {
                    if(aa.getId() != null && customerDto.getTypeCustomer().equals(TypeCustomer.PERSONAL)) {
                        return Mono.error(new IllegalArgumentException("The customer can only have one savings account"));
                    }
                    return this.save(accountDto);
                })
                .switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
    }

    public Mono<CurrentAccountDto> save(CurrentAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(pr -> {
            if (BigDecimal.valueOf(pr.getMinFixedAmount()).compareTo(accountDto.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("Insufficient minimum amount to open an account"));
            }
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
        return currentAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(currentAccount -> {
            currentAccount.setBalance(currentAccount.getBalance().add(depositDto.getAmount()));
            currentAccount.setUpdatedAt(new Date());
            depositDto.setOriginId(currentAccount.getId());
            return currentAccountRepository.save(currentAccount)
                    .flatMap(ca -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE))
                    .then(Mono.just("Deposit completed successfully"));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return currentAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber())
                .flatMap(currentAccount -> {
                    return customerClient.getClient(currentAccount.getHolderId()).flatMap(customerDto -> {
                       if(customerDto.getTypeProfile().equals(TypeProfile.VIP)) {
                           return productClient.getProductAccount(currentAccount.getProductId()).flatMap(productDto -> {
                               BigDecimal minFixedAmount = currentAccount.getBalance().subtract(withdrawDto.getAmount());
                               if (minFixedAmount.compareTo(BigDecimal.valueOf(productDto.getMinFixedAmount())) == -1 ) {
                                   return Mono.error(new IllegalArgumentException("Error the account requires a minimum amount of balance allowed"));
                               }
                               return this.execWithdraw(currentAccount, withdrawDto);
                           }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")));
                       }
                       if (withdrawDto.getAmount().compareTo(currentAccount.getBalance()) == 1 ) {
                            return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
                       }
                       return this.execWithdraw(currentAccount, withdrawDto);
                    }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> wireTransfer(OperationDto withdrawDto) {
        return null;
    }

    public Mono<String> execWithdraw(CurrentAccount currentAccount, OperationDto withdrawDto) {
        currentAccount.setBalance(currentAccount.getBalance().subtract(withdrawDto.getAmount()));
        currentAccount.setUpdatedAt(new Date());
        withdrawDto.setOriginId(currentAccount.getId());
        return currentAccountRepository.save(currentAccount)
                .flatMap(ca -> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT))
                .then(Mono.just("Withdrawal completed successfully"));
    }

    public Mono<Transaction> saveTransaction(OperationDto withdrawDto, TypeTransaction typeTxn, TypeAffectation typeAffectation){
        Transaction transaction = new Transaction();
        transaction.setOrigin(withdrawDto.getOriginId());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(withdrawDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(withdrawDto.getOperation());
        transaction.setAffectation(typeAffectation);
        return transactionRepository.insert(transaction);
    }
}
