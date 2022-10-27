package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.CurrentAccountException;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperCurrentAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.feignclient.CreditClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.CurrentAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
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
public class CurrentAccountServiceImpl implements ManyAccountService<CurrentAccountDto> {
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
            if(customerDto.getTypeProfile() != null && customerDto.getTypeProfile().equals(TypeProfile.PYME)) {
                return creditClient.getCreditCardCustomer(accountDto.getHolderId()).count()
                        .flatMap(count -> {
                            if (count > 0) {
                                return this.findAndSave(accountDto, customerDto);
                            }
                            return Mono.error(new CurrentAccountException("The customer must have a credit card to enable this account"));
                        }).switchIfEmpty(Mono.error(new CurrentAccountException("The customer must have a credit card to enable this account")));
            }
            return this.findAndSave(accountDto, customerDto);
        }).switchIfEmpty(Mono.error(new CurrentAccountException("Customer not found")));
    }
    public Mono<CurrentAccountDto> findAndSave(CurrentAccountDto accountDto, CustomerDto customerDto) {
        return currentAccountRepository.countByHolderId(accountDto.getHolderId())
                .flatMap(count -> {
                    if(count >= 1 && customerDto.getTypeCustomer().equals(TypeCustomer.PERSONAL)) {
                        return Mono.error(new CurrentAccountException("The customer can only have one savings account"));
                    }
                    return this.save(accountDto);
                })
                .switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
    }
    public Mono<CurrentAccountDto> save(CurrentAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(pr -> {
            if (BigDecimal.valueOf(pr.getMinFixedAmount()).compareTo(accountDto.getBalance()) == 1) {
                return Mono.error(new CurrentAccountException("Insufficient minimum amount to open an account"));
            }
            accountDto.setTypeAccount(TypeAccount.CURRENT_ACCOUNT);
            return mapperCurrentAccount.toCurrentAccount(accountDto)
                    .flatMap(currentAccountRepository::insert)
                    .flatMap(mapperCurrentAccount::toDto);
        });
    };
    public Flux<CurrentAccountDto> findByHolderId(String holderId) {
        return currentAccountRepository.findByHolderIdAndTypeAccount(holderId, TypeAccount.CURRENT_ACCOUNT)
                .flatMap(mapperCurrentAccount::toDto);
    }
    @Override
    public Mono<CurrentAccountDto> findByNumber(String number) {
        return currentAccountRepository.findByNumberAndTypeAccount(number, TypeAccount.CURRENT_ACCOUNT)
                .flatMap(mapperCurrentAccount::toDto);
    }
    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }
    @Override
    public Mono<Void> delete(String accountId) {
        return currentAccountRepository.findById(accountId)
                .flatMap(currentAccountRepository::delete);
    }
}
