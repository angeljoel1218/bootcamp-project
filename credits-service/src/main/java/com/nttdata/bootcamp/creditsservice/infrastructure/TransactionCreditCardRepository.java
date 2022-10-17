package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionCreditCardRepository extends ReactiveMongoRepository<TransactionCreditCard, String> {

    Flux<TransactionCreditCard> findByIdCredit(String idCredit);
}