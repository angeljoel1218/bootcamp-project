package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {

  private String sourceNumberCell;
  private String targetNumberCell;
  private BigDecimal amount;
}
