package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {


    Mono<CreditCard> create(Mono<CreditCard> creditCardMono);
    Mono<CreditCard> update( Mono<CreditCard> creditCardMono,String id);
    Mono<Void> delete(String id);
    Mono<CreditCard> findById(String id);
    Flux<CreditCard> findAll();
}
