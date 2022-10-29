package com.nttdata.bootcamp.cardservice.model.dto;

import com.nttdata.bootcamp.cardservice.model.BankAccount;
import com.nttdata.bootcamp.cardservice.model.constant.CardType;
import lombok.Data;

import java.util.Date;

@Data
public class CardDto {
    private String id;
    private String number;
    private Date expirationDate;
    private String cvv;
    private CardType cardType;
    private String holderId;
    private BankAccount[] accounts;
}
