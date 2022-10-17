package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.model.CurrentAccount;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CurrentAccountServiceImpl implements AccountService<CurrentAccountDto>{
    @Override
    public Mono<CurrentAccountDto> create(CurrentAccountDto accountDto) {
        return null;
    }

    @Override
    public Mono<CurrentAccountDto> findByHolderId(String holderId) {
        return null;
    }

    @Override
    public Flux<CurrentAccountDto> listTransactions(String accountId) {
        return null;
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return null;
    }

    @Override
    public Mono<CurrentAccountDto> deposit(CurrentAccountDto depositDto) {
        return null;
    }

    @Override
    public Mono<CurrentAccountDto> withdraw(CurrentAccountDto withdrawDto) {
        return null;
    }

    @Override
    public Mono<CurrentAccountDto> payment(CurrentAccountDto paymentDto) {
        return null;
    }
}
