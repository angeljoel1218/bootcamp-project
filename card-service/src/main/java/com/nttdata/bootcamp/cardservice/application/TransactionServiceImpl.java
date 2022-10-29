package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.CardMovement;
import com.nttdata.bootcamp.cardservice.model.dto.TransactionDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Override
    public Mono<CardMovement> payment(TransactionDto transactionDto) {
        return null;
    }

    @Override
    public Mono<CardMovement> withdraw(TransactionDto transactionDto) {
        return null;
    }
}
