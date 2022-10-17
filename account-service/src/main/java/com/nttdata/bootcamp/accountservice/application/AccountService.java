package com.nttdata.bootcamp.accountservice.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService<T> {
    public Mono<T> create(T accountDto);
    public Mono<T> findByHolderId(String holderId);
    public Flux<T> listTransactions(String accountId);
    public Mono<Void> delete(String accountId);
    public Mono<T> deposit(T depositDto);
    public Mono<T> withdraw(T withdrawDto);
    public Mono<T> payment(T paymentDto);
}
