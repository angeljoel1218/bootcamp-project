package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
public interface CreditService extends GeneralService<Credit> {

    Mono<CreditDues> payment(CreditDuesDto creditDuesDto);

    Flux<CreditDues> findCreditDuesByIdCredit(String idCredit);


}
