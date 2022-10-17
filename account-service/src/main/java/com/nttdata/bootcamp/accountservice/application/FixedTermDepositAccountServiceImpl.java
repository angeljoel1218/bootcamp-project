package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.model.dto.FixedTermDepositAccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FixedTermDepositAccountServiceImpl implements AccountService<FixedTermDepositAccountDto>{
    @Override
    public Mono<FixedTermDepositAccountDto> create(FixedTermDepositAccountDto accountDto) {
        return null;
    }

    @Override
    public Mono<FixedTermDepositAccountDto> findByHolderId(String holderId) {
        return null;
    }

    @Override
    public Flux<FixedTermDepositAccountDto> listTransactions(String accountId) {
        return null;
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return null;
    }

    @Override
    public Mono<FixedTermDepositAccountDto> deposit(FixedTermDepositAccountDto depositDto) {
        return null;
    }

    @Override
    public Mono<FixedTermDepositAccountDto> withdraw(FixedTermDepositAccountDto withdrawDto) {
        return null;
    }

    @Override
    public Mono<FixedTermDepositAccountDto> payment(FixedTermDepositAccountDto paymentDto) {
        return null;
    }
}
