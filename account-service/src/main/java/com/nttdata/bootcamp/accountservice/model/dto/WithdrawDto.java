package com.nttdata.bootcamp.accountservice.model.dto;


import java.math.BigDecimal;
import lombok.Data;

@Data
public class WithdrawDto {
  private BigDecimal amount;
  private String accountNumber;
  private String operation;
}
