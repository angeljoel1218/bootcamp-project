package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.Client;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

    Mono<Credit> create(Mono<Credit> creditMono);
    Mono<Credit> update( Mono<Credit> creditMono,String id);
    Mono<Void> delete(String id);
    Mono<Credit> findById(String id);
    Flux<Credit> findAll();

    Mono<Object> findClientById(String id);
}
