package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class WithdrawDto {
    private String cardNumber;
    private String accountNumber;
    private BigDecimal amount;
    private String detail;
    private String entity;
}
