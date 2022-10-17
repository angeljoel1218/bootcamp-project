package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperSavingAccount;
import com.nttdata.bootcamp.accountservice.client.ProductClient;
import com.nttdata.bootcamp.accountservice.client.UserClient;
import com.nttdata.bootcamp.accountservice.model.SavingAccount;
import com.nttdata.bootcamp.accountservice.model.TypeClient;
import com.nttdata.bootcamp.accountservice.model.dto.Client;
import com.nttdata.bootcamp.accountservice.model.dto.SavingAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SavingAccountServiceImpl implements AccountService<SavingAccountDto> {
    @Autowired
    MapperSavingAccount mapperSavingAccount;

    @Autowired
    ProductClient productClient;

    @Autowired
    UserClient userClient;

    @Override
    public Mono<SavingAccountDto> create(SavingAccountDto accountDto) {
        Client client = userClient.getClient(accountDto.getHolderId()).block();
        return null;
    }

    @Override
    public Mono<SavingAccountDto> findByHolderId(String holderId) {
        return null;
    }

    @Override
    public Flux<SavingAccountDto> listTransactions(String accountId) {
        return null;
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return null;
    }

    @Override
    public Mono<SavingAccountDto> deposit(SavingAccountDto depositDto) {
        return null;
    }

    @Override
    public Mono<SavingAccountDto> withdraw(SavingAccountDto withdrawDto) {
        return null;
    }

    @Override
    public Mono<SavingAccountDto> payment(SavingAccountDto paymentDto) {
        return null;
    }
}
