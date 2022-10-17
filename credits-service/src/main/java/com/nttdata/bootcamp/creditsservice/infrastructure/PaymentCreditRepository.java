package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.PaymentCredit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PaymentCreditRepository extends ReactiveMongoRepository<PaymentCredit, String> {

    Flux<PaymentCredit> findByIdCredit(String id);

}
