package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import java.util.Date;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * javadoc.
 * Bank
 * @since 2022
 */

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

  public Flux<CreditCard> findByIdCustomer(String id);

  public  Flux<CreditCard> findByCreateDateBetweenAndIdProduct(Date start,
                                                               Date end,
                                                               String idProduct);
}
