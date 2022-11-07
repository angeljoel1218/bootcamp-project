package com.nttdata.bootcamp.cardservice.application.controller;

import com.nttdata.bootcamp.cardservice.application.CardService;
import com.nttdata.bootcamp.cardservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
@RefreshScope
@RestController
public class CardController {
    @Autowired
    CardService cardService;

    @PostMapping("card/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CardDto> register(@RequestBody CardDto cardDto){
        return cardService.registerCard(cardDto);
    }

    @PutMapping("card/account/{cardId}")
    public Mono<BankAccountDto> attachAccount(@PathVariable("cardId") String cardId,
                                              @RequestBody AttachAccountDto attachAccountDto) {
        return cardService.attachAccount(cardId, attachAccountDto);
    }

    @GetMapping("card/account/balance/{cardNumber}")
    public Mono<AccountDto> mainAccountBalance(@PathVariable("cardNumber") String cardNumber) {
        return cardService.mainAccountBalance(cardNumber);
    }

    @GetMapping("card/{cardId}/movements")
    public Flux<CardMovementDto> listMovements(@PathVariable("cardId") String cardId) {
        return cardService.listMovements(cardId);
    }
}
