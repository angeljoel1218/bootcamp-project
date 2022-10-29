package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private String cardNumber;
    private BigDecimal amount;
    private String detail;
    private String entity;
}
