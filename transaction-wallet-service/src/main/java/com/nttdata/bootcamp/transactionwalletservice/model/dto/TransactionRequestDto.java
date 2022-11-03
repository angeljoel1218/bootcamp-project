package com.nttdata.bootcamp.transactionwalletservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {
    private String sourceNumberCell;
    private String targetNumberCell;
    private BigDecimal amount;
}
