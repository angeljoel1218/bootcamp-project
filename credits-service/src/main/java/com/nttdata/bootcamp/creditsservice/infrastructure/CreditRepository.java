package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import java.util.Date;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * javadoc.
 * Bank
 * @since 2022
 */
public interface CreditRepository extends ReactiveMongoRepository<Credit, String > {
  public Flux<Credit> findByIdCustomer(String id);

  public  Flux<Credit> findByCreateDateBetweenAndIdProduct(Date start, Date end, String idProduct);
}
