package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.FixedTermAccountException;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperFixedTerm;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.CustomerClientService;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.feignclient.ProductClientService;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.constant.StateAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeCustomer;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class FixedTermAccountServiceImpl implements SingleAccountService<FixedTermAccountDto> {
    @Autowired
    CustomerClientService customerClient;
    @Autowired
    ProductClientService productClient;
    @Autowired
    FixedTermAccountRepository fixedTermAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MapperFixedTerm mapperFixedTerm;
    @Autowired
    MapperTransaction mapperTransaction;
    @Override
    public Mono<FixedTermAccountDto> create(FixedTermAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClient.getCustomer(accountDto.getHolderId()).flatMap(client -> {
            if(client.getTypeCustomer().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new FixedTermAccountException("Customer must be personal type"));
            }
            return fixedTermAccountRepository.countByHolderIdAndTypeAccount(accountDto.getHolderId(), TypeAccount.FIXED_TERM_ACCOUNT).flatMap(count -> {
                if (count >= 1) {
                    return Mono.error(new FixedTermAccountException("The customer can only have one fixed term deposit account"));
                }
                return this.save(accountDto);
            }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
        }).switchIfEmpty(Mono.error(new FixedTermAccountException("Customer not found")));
    }
    public Mono<FixedTermAccountDto> save(FixedTermAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            if (BigDecimal.valueOf(productAccountDto.getMinFixedAmount()).compareTo(accountDto.getBalance()) == 1) {
                return Mono.error(new FixedTermAccountException("Insufficient minimum amount to open an account"));
            }
            accountDto.setTypeAccount(TypeAccount.FIXED_TERM_ACCOUNT);
            Mono<FixedTermAccount> fixedTermDepositAccountMono = mapperFixedTerm.toProductAccount(accountDto)
                    .flatMap(fixedTermAccountRepository::insert);
            return fixedTermDepositAccountMono
                    .flatMap(mapperFixedTerm::toDto);
        });
    };
    @Override
    public Mono<FixedTermAccountDto> findByHolderId(String holderId) {
        return fixedTermAccountRepository.findByHolderIdAndTypeAccount(holderId, TypeAccount.FIXED_TERM_ACCOUNT)
                .flatMap(mapperFixedTerm::toDto);
    }
    @Override
    public Mono<FixedTermAccountDto> findByNumber(String number) {
        return fixedTermAccountRepository.findByNumberAndTypeAccount(number, TypeAccount.FIXED_TERM_ACCOUNT)
                .flatMap(mapperFixedTerm::toDto);
    }
    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }
    @Override
    public Mono<Void> delete(String accountId) {
        return fixedTermAccountRepository.findById(accountId)
                .flatMap(fixedTermAccountRepository::delete);
    }
}
