package com.nttdata.bootcamp.walletservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */

@Data
public class TransactionDto {
  private String sourceNumberCell;
  private String targetNumberCell;
  private BigDecimal amount;
}
