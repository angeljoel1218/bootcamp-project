package com.nttdata.bootcamp.creditsservice.infrastructure;

import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditDuesRepository extends ReactiveMongoRepository<CreditDues, String> {

    Flux<CreditDues> findByIdCredit(String id);

    Mono<CreditDues> findByIdCreditAndNroDues(String idCredit, Integer nroDues);

    Mono<Void> deleteByIdCredit(String id);



    Mono<CreditDues> findFirstByIdCreditAndStatusOrderByExpirationDateAsc(String idCredit, CreditStatus status);


}
