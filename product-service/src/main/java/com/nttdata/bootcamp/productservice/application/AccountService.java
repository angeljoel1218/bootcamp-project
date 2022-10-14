package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> create(Mono<Account> account);
    Mono<Account> update(String id, Account account);
    Mono<Void> delete(String id);
    Mono<Account> findById(String id);
    Flux<Account> findAll();
}
