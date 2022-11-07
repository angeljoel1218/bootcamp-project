package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.CreditCardMonthly;
import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
public interface CreditCardMonthlyRepository
  extends ReactiveMongoRepository<CreditCardMonthly, String> {

  Mono<CreditCardMonthly>
  findFirstByIdCreditCardOrderByEndDateDesc(
    String idCreditCard);


  Mono<CreditCardMonthly>
  findFirstByIdCreditCardAndStatusOrderByEndDateAsc(
    String idCreditCard,
    CreditStatus status);
}
