package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 * @since 2022
 */
public interface CreditCardService extends GeneralService<CreditCard> {
    Mono<String> payment(TransactionCreditCard TransactionCredit);
    Mono<String> charge(TransactionCreditCard TransactionCredit);
    Flux<TransactionCreditCard> findTransactionByIdCredit(String id);
    Mono<List<TransactionCreditCard>> findLastTransactionByIdCredit(String idCredit, Integer limit);

}
