package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {
    private BigDecimal amount;
    private String sourceAccount;
    private String targetAccount;
    private String operation;
}
