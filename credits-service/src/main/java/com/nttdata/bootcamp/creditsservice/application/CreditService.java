package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.Customer;
import com.nttdata.bootcamp.creditsservice.model.TransactionCredit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CreditService {

    Mono<Credit> create(Credit creditMono);
    Mono<Credit> update( Mono<Credit> creditMono,String id);
    Mono<Void> delete(String id);
    Mono<Credit> findById(String id);
    Flux<Credit> findAll();
    Mono<TransactionCredit> payment(TransactionCredit TransactionCredit);



}
