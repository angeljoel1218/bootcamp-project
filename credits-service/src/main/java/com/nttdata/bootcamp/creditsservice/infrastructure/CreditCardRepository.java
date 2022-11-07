package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Date;


/**
 *
 * @since 2022
 */
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

    public Flux<CreditCard> findByIdCustomer(String id);

    public  Flux<CreditCard> findByCreateDateBetweenAndIdProduct(Date start,
                                                                 Date end,
                                                                 String idProduct);
}
