package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawTxnDto {
    private BigDecimal amount;
    private String accountNumber;
    private String operation;
}
