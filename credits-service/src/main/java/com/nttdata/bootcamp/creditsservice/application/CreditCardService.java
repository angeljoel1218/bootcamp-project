package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.TransactionCredit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {


    Mono<CreditCard> create(CreditCard creditCard);
    Mono<CreditCard> update( Mono<CreditCard> creditCardMono,String id);
    Mono<Void> delete(String id);
    Mono<CreditCard> findById(String id);
    Flux<CreditCard> findAll();

    Mono<TransactionCredit> payment(TransactionCredit TransactionCredit);
    Mono<TransactionCredit> charge(TransactionCredit TransactionCredit);
}
