package com.nttdata.bootcamp.cardservice.application.mapper;

import com.nttdata.bootcamp.cardservice.model.BankAccount;
import com.nttdata.bootcamp.cardservice.model.Card;
import com.nttdata.bootcamp.cardservice.model.CardMovement;
import com.nttdata.bootcamp.cardservice.model.dto.BankAccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperCard {
    public Mono<CardDto> toDto(Card card) {
        ModelMapper modelMapper = new ModelMapper();
        CardDto cardDto = modelMapper.map(card, CardDto.class);
        return Mono.just(cardDto);
    }

    public Mono<Card> toCard(CardDto cardDto) {
        ModelMapper modelMapper = new ModelMapper();
        Card card = modelMapper.map(cardDto, Card.class);
        return Mono.just(card);
    }

    public BankAccountDto toBankAccountDto(BankAccount bankAccount) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(bankAccount, BankAccountDto.class);
    }

    public Mono<CardMovementDto> toCardMovementDto(CardMovement cardMovement) {
        ModelMapper modelMapper = new ModelMapper();
        CardMovementDto cardMovementDto = modelMapper.map(cardMovement, CardMovementDto.class);
        return Mono.just(cardMovementDto);
    }
}
