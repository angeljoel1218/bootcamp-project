package com.nttdata.bootcamp.accountservice.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * javadoc.
 * @since 2022
 */
public interface GenericAccountService<T> {
  public Mono<T> create(T accountDto);

  public Mono<Void> delete(String accountId);

}
