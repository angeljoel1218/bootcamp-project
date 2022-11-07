package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.constant.StateTransaction;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionBootcoinDto {
  private String sourceNumber;
  private String targetNumber;
  private BigDecimal amount;
  private String transactionId;
  private String detail;
  private StateTransaction state;
}
