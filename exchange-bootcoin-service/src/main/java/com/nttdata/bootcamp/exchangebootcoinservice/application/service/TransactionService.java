package com.nttdata.bootcamp.exchangebootcoinservice.application.service;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.TransactionDto;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionDto> acceptRequest(String orderId);
}
