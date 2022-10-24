package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.PaymentCredit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService extends GeneralService<Credit> {

    Mono<PaymentCredit> payment(PaymentCredit paymentCredit);

    Flux<PaymentCredit> findPaymentByIdCredit(String id);


}
