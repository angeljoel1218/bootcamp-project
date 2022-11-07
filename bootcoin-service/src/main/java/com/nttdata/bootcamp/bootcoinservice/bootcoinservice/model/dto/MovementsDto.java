package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Some javadoc.
 *
 * @since 2022
 */
@Data
@Builder
public class MovementsDto {
  private String bootcoinId;
  private String phone;
  private String description;
  private BigDecimal amount;
  private LocalDate date;
}
