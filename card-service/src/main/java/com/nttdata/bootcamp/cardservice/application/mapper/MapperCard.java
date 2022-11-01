package com.nttdata.bootcamp.cardservice.application.mapper;

import com.nttdata.bootcamp.cardservice.model.BankAccount;
import com.nttdata.bootcamp.cardservice.model.Card;
import com.nttdata.bootcamp.cardservice.model.CardMovement;
import com.nttdata.bootcamp.cardservice.model.dto.BankAccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardDto;
import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import com.nttdata.bootcamp.cardservice.model.dto.WithdrawDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperCard {
    public CardDto toDto(Card card) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(card, CardDto.class);
    }

    public Card toCard(CardDto cardDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(cardDto, Card.class);
    }

    public BankAccountDto toBankAccountDto(BankAccount bankAccount) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(bankAccount, BankAccountDto.class);
    }

    public CardMovementDto toCardMovementDto(CardMovement cardMovement) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(cardMovement, CardMovementDto.class);
    }

    public CardMovement toCardMovement(WithdrawDto withdrawDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(withdrawDto, CardMovement.class);
    }
}
