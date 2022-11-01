package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import com.nttdata.bootcamp.cardservice.model.dto.PaymentDto;
import com.nttdata.bootcamp.cardservice.model.dto.WithdrawDto;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<CardMovementDto> payment(PaymentDto paymentDto);
    Mono<CardMovementDto> withdraw(WithdrawDto withdrawDto);
}
