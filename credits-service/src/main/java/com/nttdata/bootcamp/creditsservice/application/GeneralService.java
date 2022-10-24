package com.nttdata.bootcamp.creditsservice.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GeneralService <T>{
    Mono<T> create(T credit);
    Mono<T> update( Mono<T> credit,String id);
    Mono<Void> delete(String id);
    Mono<T> findById(String id);
    Flux<T> findAll();
    Flux<T> findByIdCustomer(String id);


}
