package com.nttdata.bootcamp.transactionwalletservice.application.servivce;

import com.nttdata.bootcamp.transactionwalletservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.transactionwalletservice.model.dto.TransactionRequestDto;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionDto> transfer(TransactionRequestDto transactionRequestDto);
}
