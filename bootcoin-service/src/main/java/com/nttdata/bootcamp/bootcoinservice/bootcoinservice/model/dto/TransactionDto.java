package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Some javadoc.
 *
 * @since 2022
 */
@Data
public class TransactionDto {

  private String sourceWalletId;
  private String targetWalletId;
  private BigDecimal amount;
}
