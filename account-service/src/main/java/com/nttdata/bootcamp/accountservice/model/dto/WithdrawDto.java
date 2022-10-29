package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawDto {
    private BigDecimal amount;
    private String accountNumber;
    private String operation;
}
