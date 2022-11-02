package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositDto {
  private BigDecimal amount;
  private String targetAccount;
  private String operation;
}
