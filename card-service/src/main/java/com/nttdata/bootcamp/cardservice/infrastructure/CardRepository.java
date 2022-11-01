package com.nttdata.bootcamp.cardservice.infrastructure;

import com.nttdata.bootcamp.cardservice.model.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CardRepository extends ReactiveMongoRepository<Card, String> {
    Mono<Card> findByNumber(String number);
}
