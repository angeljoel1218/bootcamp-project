package com.nttdata.bootcamp.accountservice.application;

import reactor.core.publisher.Flux;

public interface ManyAccountService<T> extends AccountService<T> {
    public Flux<T> findByHolderId(String holderId);
}
