package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
}
