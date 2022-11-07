package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class TransactionCreditDto {
    private  String idCredit;
    private String description;
    private BigDecimal amount;
}
