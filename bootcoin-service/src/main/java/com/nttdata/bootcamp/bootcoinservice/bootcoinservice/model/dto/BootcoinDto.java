package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BootcoinDto {

  private String numberDocument;
  private String documentType;
  private String phone;
  private String names;
  private BigDecimal balance;
  private String email;

}
