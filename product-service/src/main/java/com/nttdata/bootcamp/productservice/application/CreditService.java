package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService{
    Mono<Credit> create(Mono<Credit> credit);
    Mono<Credit> update(String id, Credit credit);
    Mono<Void> delete(String id);
    Mono<Credit> findById(String id);
    Flux<Credit> findAll();
}
