package com.nttdata.bootcamp.cardservice.infrastructure;

import com.nttdata.bootcamp.cardservice.model.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CardRepository extends ReactiveMongoRepository<Card, String> {
}
