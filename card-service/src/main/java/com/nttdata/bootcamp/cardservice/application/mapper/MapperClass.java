package com.nttdata.bootcamp.cardservice.application.mapper;

import com.nttdata.bootcamp.cardservice.model.BankAccount;
import com.nttdata.bootcamp.cardservice.model.Card;
import com.nttdata.bootcamp.cardservice.model.CardMovement;
import com.nttdata.bootcamp.cardservice.model.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @since 2022
 */
@Component
public class MapperClass {
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

    public WithdrawDto toWithdrawDto(PaymentDto paymentDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(paymentDto, WithdrawDto.class);
    }

    public CreditDuesRequestDto toCreditDuesRequestDto(PaymentDto paymentDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(paymentDto, CreditDuesRequestDto.class);
    }

    public TransactionCreditDto toTransactionCreditDto(PaymentDto paymentDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(paymentDto, TransactionCreditDto.class);
    }
}
