package com.nttdata.bootcamp.accountservice.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransactionRequestDto {
  private BigDecimal amount;
  private String sourceAccount;
  private String targetAccount;
  private String operation;
}
