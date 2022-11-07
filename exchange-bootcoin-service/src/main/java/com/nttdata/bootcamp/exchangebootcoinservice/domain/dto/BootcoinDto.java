package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * Some javadoc.
 *
 * @since 2022
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcoinDto {

  private String numberDocument;

  private String documentType;

  private String phone;

  private String names;

  private BigDecimal balance;

  private String email;
}
