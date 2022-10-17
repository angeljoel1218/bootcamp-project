package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String > {
}
