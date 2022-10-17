package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.TransactionCredit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionCreditRepository extends ReactiveMongoRepository<TransactionCredit, String> {

    Flux<TransactionCredit> findByIdCredit(String idCredit);
}
