package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.SavingsAccountException;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperSavingsAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.feignclient.CreditClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.constant.StateAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeCustomer;
import com.nttdata.bootcamp.accountservice.model.constant.TypeProfile;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SavingsAccountServiceImpl implements SingleAccountService<SavingsAccountDto> {
    @Autowired
    MapperSavingsAccount mapperSavingsAccount;

    @Autowired
    MapperTransaction mapperTransaction;

    @Autowired
    ProductClient productClient;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    CreditClient creditClient;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<SavingsAccountDto> create(SavingsAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getTypeCustomer().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new SavingsAccountException("Customer must be personal type"));
            }

            if(client.getTypeProfile().equals(TypeProfile.VIP)) {
                return creditClient.getCreditCardCustomer(accountDto.getHolderId())
                        .flatMap(creditCardDto -> {
                           if(creditCardDto.getId() != null) {
                               return this.findAndSave(accountDto);
                           }
                           return Mono.error(new SavingsAccountException("The customer must have a credit card to enable this account"));
                        }).switchIfEmpty(Mono.error(new SavingsAccountException("The customer must have a credit card to enable this account")));
            }

            return this.findAndSave(accountDto);
        }).switchIfEmpty(Mono.error(new SavingsAccountException("Customer not found")));
    }
    public Mono<SavingsAccountDto> findAndSave(SavingsAccountDto accountDto) {
        return savingsAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(savingsAccount -> {
            if (savingsAccount.getId() != null) {
                return Mono.error(new SavingsAccountException("The customer can only have one savings account"));
            }
            return this.save(accountDto);
        }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
    }
    public Mono<SavingsAccountDto> save(SavingsAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            if (accountDto.getBalance().compareTo(BigDecimal.valueOf(productAccountDto.getMinFixedAmount())) >= 0) {
                return Mono.error(new SavingsAccountException("Insufficient minimum amount to open an account"));
            }
            accountDto.setTypeAccount(TypeAccount.SAVINGS_ACCOUNT);
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
}
