package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
public interface CardService {
    Mono<CardDto> registerCard(CardDto cardDto);
    Flux<CardMovementDto> listMovements(String cardId);
    Mono<AccountDto> mainAccountBalance(String cardNumber);
    Mono<BankAccountDto> attachAccount(String cardId, AttachAccountDto attachAccountDto);
}
