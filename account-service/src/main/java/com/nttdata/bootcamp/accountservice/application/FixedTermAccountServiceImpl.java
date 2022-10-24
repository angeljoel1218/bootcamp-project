package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperFixedTerm;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermAccountRepository;
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
public class FixedTermAccountServiceImpl implements AccountOneService<FixedTermAccountDto> {
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
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
        return customerClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getTypeCustomer().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Customer must be personal type"));
            }
            return fixedTermAccountRepository.countByHolderId(accountDto.getHolderId()).flatMap(count -> {
                if (count >= 1) {
                    return Mono.error(new IllegalArgumentException("The customer can only have one fixed term deposit account"));
                }
                return this.save(accountDto);
            }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }

    public Mono<FixedTermAccountDto> save(FixedTermAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            if (BigDecimal.valueOf(productAccountDto.getMinFixedAmount()).compareTo(accountDto.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("Insufficient minimum amount to open an account"));
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
        return fixedTermAccountRepository.findByHolderId(holderId)
                .flatMap(mapperFixedTerm::toDto);
    }

    @Override
    public Mono<FixedTermAccountDto> findByNumber(String number) {
        return fixedTermAccountRepository.findByNumber(number)
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
