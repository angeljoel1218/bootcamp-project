package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exception.AccountException;
import com.nttdata.bootcamp.accountservice.application.mapper.MapperSavingsAccount;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.*;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.constant.StateAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeCustomer;
import com.nttdata.bootcamp.accountservice.model.constant.TypeProfile;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService<SavingsAccountDto> {
    @Autowired
    MapperSavingsAccount mapperSavingsAccount;

    @Autowired
    ProductClientService productClientService;

    @Autowired
    CustomerClientService customerClientService;

    @Autowired
    CreditClientService creditClientService;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Override
    public Mono<SavingsAccountDto> create(SavingsAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClientService.getCustomer(accountDto.getHolderId())
                .switchIfEmpty(Mono.error(new AccountException("Customer not found")))
                .doOnNext(customerDto -> {
                    if(customerDto.getTypeCustomer().equals(TypeCustomer.COMPANY)) {
                        throw new AccountException("Customer must be personal type");
                    }
                })
                .flatMap(customerDto -> {
                    if(customerDto.getTypeProfile() != null && customerDto.getTypeProfile().equals(TypeProfile.VIP)) {
                        return creditClientService.getCreditCardCustomer(accountDto.getHolderId())
                                .switchIfEmpty(Mono.error(new AccountException("The customer must have a credit card to enable this account")))
                                .count()
                                .doOnNext(count -> {
                                    if (count == 0) {
                                        throw new AccountException("The customer must have a credit card to enable this account");
                                    }
                                })
                                .flatMap(count -> this.findAndSave(accountDto));
                    }
                    return this.findAndSave(accountDto);
                });
    }
    public Mono<SavingsAccountDto> findAndSave(SavingsAccountDto accountDto) {
        return savingsAccountRepository.findByHolderIdAndTypeAccount(accountDto.getHolderId(), TypeAccount.SAVINGS_ACCOUNT)
                .doOnNext(savingsAccount -> {
                    if (savingsAccount.getId() != null) {
                        throw new AccountException("The customer can only have one savings account");
                    }
                })
                .flatMap(savingsAccount -> this.save(accountDto))
                .switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
    }
    public Mono<SavingsAccountDto> save(SavingsAccountDto accountDto) {
        return productClientService.getProductAccount(accountDto.getProductId())
                .doOnNext(productDto -> {
                    if (accountDto.getBalance().compareTo(BigDecimal.valueOf(productDto.getMinFixedAmount())) == -1) {
                        throw new AccountException("Insufficient minimum amount to open an account");
                    }
                })
                .flatMap(productAccountDto -> {
                    accountDto.setTypeAccount(TypeAccount.SAVINGS_ACCOUNT);
                    Mono<SavingsAccount> savingsAccountMono = mapperSavingsAccount.toSavingsAccount(accountDto)
                            .flatMap(savingsAccountRepository::insert);
                    return savingsAccountMono.flatMap(mapperSavingsAccount::toDto);
                });
    };
    @Override
    public Mono<Void> delete(String accountId) {
        return savingsAccountRepository.findById(accountId)
                .flatMap(savingsAccountRepository::delete);
    }
}
