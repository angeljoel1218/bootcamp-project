package com.nttdata.bootcamp.accountservice.application;

import reactor.core.publisher.Mono;

public interface SingleAccountService<T> extends AccountService<T> {
    public Mono<T> findByHolderId(String holderId);
}
