package com.nttdata.bootcamp.accountservice.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface AccountService<T> extends GenericAccountService<T> {
  public Mono<T> findByNumber(String number);

  public Flux<T> findByHolderId(String holderId);

  public Flux<T> findByCreateDateBetweenAndProductId(Date startDate, Date endDate, String productId);
}
