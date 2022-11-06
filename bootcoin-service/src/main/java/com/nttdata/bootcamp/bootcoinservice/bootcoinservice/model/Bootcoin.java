package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Some javadoc.
 *
 * @since 2022
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Bootcoin {

  @Id
  private String id;
  private String numberDocument;
  private String documentType;
  private String phone;
  private String names;
  private BigDecimal balance;
  private String email;
  private LocalDate createAt;

}
