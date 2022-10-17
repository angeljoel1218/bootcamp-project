package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.PaymentCredit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PaymentCreditRepository extends ReactiveMongoRepository<PaymentCredit, String> {

}
