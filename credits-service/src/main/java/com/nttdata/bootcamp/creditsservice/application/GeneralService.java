package com.nttdata.bootcamp.creditsservice.application;

import java.util.Date;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * javadoc.
 * Bank
 * @since 2022
 * @param <T> types account
 */
public interface GeneralService <T> {

  Mono<T> create(T credit);

  Mono<T> update(Mono<T> credit, String id);

  Mono<Void> delete(String id);

  Mono<T> findById(String id);

  Flux<T> findAll();

  Flux<T> findByIdCustomer(String id);

  Mono<Boolean> findIsCustomerHaveDebs(String idCustomer);

  Flux<T> findByCreateDateBetweenAndIdProduct(Date start, Date end, String idProduct);


}
