package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MovementsDto {
  private String phone;
  private String description;
  private BigDecimal amount;
  private LocalDate date;
}
