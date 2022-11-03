package com.nttdata.bootcamp.walletservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {

  private String id;
  private String phone;
  private BigDecimal amount;
}
