package com.nttdata.bootcamp.creditsservice.application;


import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * javadoc.
 * Bank
 * @since 2022
 */
public interface CreditCardService extends GeneralService<CreditCard> {
  Mono<String> payment(TransactionCreditCard transactionCreditCard);

  Mono<String> charge(TransactionCreditCard transactionCreditCard);

  Flux<TransactionCreditCard> findTransactionByIdCredit(String id);

  Mono<List<TransactionCreditCard>> findLastTransactionByIdCredit(String idCredit, Integer limit);

}
