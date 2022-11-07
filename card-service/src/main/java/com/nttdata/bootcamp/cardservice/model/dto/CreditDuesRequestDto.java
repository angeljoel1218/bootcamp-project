package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class CreditDuesRequestDto {
    private  String idCredit;
    private Integer nroDues;
    private BigDecimal amount;
}
