package com.nttdata.bootcamp.reportservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditDuesDto {

  private  String idCredit;
  private Integer nroDues;
  private BigDecimal amount;
}
