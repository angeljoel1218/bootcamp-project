package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import com.nttdata.bootcamp.cardservice.model.dto.PaymentDto;
import com.nttdata.bootcamp.cardservice.model.dto.WithdrawDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionService {
  Mono<CardMovementDto> payment(PaymentDto paymentDto);

  Mono<CardMovementDto> withdraw(WithdrawDto withdrawDto);

  Mono<List<CardMovementDto>>  findLastByCardIdOrderByOperationDateDesc(String cardId, Integer top);
}
