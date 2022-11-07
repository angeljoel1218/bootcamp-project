package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class WithdrawTxnDto {
    private BigDecimal amount;
    private String accountNumber;
    private String operation;
}
