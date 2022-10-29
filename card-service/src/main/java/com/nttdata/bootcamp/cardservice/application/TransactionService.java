package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.CardMovement;
import com.nttdata.bootcamp.cardservice.model.dto.TransactionDto;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<CardMovement> payment(TransactionDto transactionDto);
    Mono<CardMovement> withdraw(TransactionDto transactionDto);
}
