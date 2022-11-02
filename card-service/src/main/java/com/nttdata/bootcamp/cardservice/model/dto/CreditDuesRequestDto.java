package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditDuesRequestDto {
    private  String idCredit;
    private Integer nroDues;
    private BigDecimal amount;
}
