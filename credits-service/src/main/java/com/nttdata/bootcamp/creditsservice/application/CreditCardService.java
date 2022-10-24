package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.PaymentCredit;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService extends GeneralService<CreditCard> {
    Mono<TransactionCreditCard> payment(TransactionCreditCard TransactionCredit);
    Mono<TransactionCreditCard> charge(TransactionCreditCard TransactionCredit);
    Flux<TransactionCreditCard> findTransactionByIdCredit(String id);

}
