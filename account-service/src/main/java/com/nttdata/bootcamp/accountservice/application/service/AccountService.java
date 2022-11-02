package com.nttdata.bootcamp.accountservice.application.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService<T> extends GenericAccountService<T>{
    public Mono<T> findByNumber(String number);
    public Flux<T> findByHolderId(String holderId);
}
