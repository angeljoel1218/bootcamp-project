package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.model.dto.AccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.AttachAccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardServiceImpl implements CardService{
    @Override
    public Mono<CardDto> registerCard(CardDto cardDto) {
        return null;
    }

    @Override
    public Flux<CardMovementDto> listMovements(String cardNumber) {
        return null;
    }

    @Override
    public Mono<AccountDto> mainAccountBalance(String cardNumber) {
        return null;
    }

    @Override
    public Mono<AttachAccountDto> attachAccount(AttachAccountDto attachAccountDto) {
        return null;
    }
}
