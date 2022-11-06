package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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

  @NotBlank(message = "numberDocument is required")
  private String numberDocument;

  @NotBlank(message = "documentType is required")
  private String documentType;

  @NotBlank(message = "phone is required")
  private String phone;

  @NotBlank(message = "names is required")
  private String names;

  private BigDecimal balance;

  @NotBlank(message = "email is required")
  private String email;

}
