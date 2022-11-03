package com.nttdata.bootcamp.wallettransactionservice.application.servivce;

import com.nttdata.bootcamp.wallettransactionservice.model.LastTransaction;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionRequestDto;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionDto> transfer(TransactionRequestDto transactionRequestDto);
    Mono<LastTransaction> lastTransaction(String walletId);
}
