package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.Customer;
import com.nttdata.bootcamp.creditsservice.model.PaymentCredit;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

    Mono<Credit> create(Credit creditMono);
    Mono<Credit> update( Mono<Credit> creditMono,String id);
    Mono<Void> delete(String id);
    Mono<Credit> findById(String id);
    Flux<Credit> findAll();
    Mono<PaymentCredit> payment(PaymentCredit paymentCredit);

    Mono<Customer> findCustomerById(String id);

    Flux<Credit> findByIdCustomer(String id);

    Flux<PaymentCredit> findPaymentByIdCredit(String id);


}
