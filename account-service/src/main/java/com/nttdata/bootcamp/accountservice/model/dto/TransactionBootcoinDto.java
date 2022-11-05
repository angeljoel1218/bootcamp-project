package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionBootcoinDto {
  private String sourceNumber;
  private String targetNumber;
  private BigDecimal amount;
  private String transactionId;
  private Status status;

  public enum  Status {
    PENDING, COMPLETED, REFUSED
  }
}
