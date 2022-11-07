package com.nttdata.bootcamp.creditsservice.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 *
 * @since 2022
 *  @param <T> Credit
 */
public interface GeneralService <T>{
    Mono<T> create(T credit);
    Mono<T> update( Mono<T> credit, String id);
    Mono<Void> delete(String id);
    Mono<T> findById(String id);
    Flux<T> findAll();
    Flux<T> findByIdCustomer(String id);
    Mono<Boolean> findIsCustomerHaveDebs(String idCustomer);
    Flux<T> findByCreateDateBetweenAndIdProduct(Date start, Date end, String idProduct);


}
