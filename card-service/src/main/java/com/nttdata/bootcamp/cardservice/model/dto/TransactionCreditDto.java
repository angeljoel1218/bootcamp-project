package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCreditDto {
    private  String idCredit;
    private String description;
    private BigDecimal amount;
}
