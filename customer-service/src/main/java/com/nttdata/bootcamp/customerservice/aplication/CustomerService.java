package com.nttdata.bootcamp.customerservice.aplication;

import com.nttdata.bootcamp.customerservice.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CustomerService {

    Mono<Customer> create(Mono<Customer> clientMono);

    Flux<Customer> findAll();

    Mono<Customer> findById(String id);

    Mono<Customer> update(Mono<Customer> clientMono, String id);

    Mono<Void> delete(String id);

}
