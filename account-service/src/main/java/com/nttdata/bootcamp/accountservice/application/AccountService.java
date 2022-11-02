package com.nttdata.bootcamp.accountservice.application;

import java.util.Date;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * javadoc.
 * @since 2022
 */

public interface AccountService<T> extends GenericAccountService<T> {
  public Mono<T> findByNumber(String number);

  public Flux<T> findByHolderId(String holderId);

  public Flux<T> findByCreateDateBetweenAndProductId(Date startDate,
                                                     Date endDate,
                                                     String productId);
}
