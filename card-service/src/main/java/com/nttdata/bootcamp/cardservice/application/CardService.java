package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.dto.AccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.AttachAccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<CardDto> registerCard(CardDto cardDto);
    Flux<CardMovementDto> listMovements(String cardNumber);
    Mono<AccountDto> mainAccountBalance(String cardNumber);
    Mono<AttachAccountDto> attachAccount(AttachAccountDto attachAccountDto);
}
