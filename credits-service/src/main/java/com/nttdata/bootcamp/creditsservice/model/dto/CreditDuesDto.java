package com.nttdata.bootcamp.creditsservice.model.dto;

import java.math.BigDecimal;
import lombok.Data;

/**
 * javadoc.
 * Bank
 * @since 2022
 */
@Data
public class CreditDuesDto {

  private  String idCredit;
  private Integer nroDues;
  private BigDecimal amount;
}
