package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@Document(collation = "bootMovements")
public class BootcoinMovements {

  @Id
  private String id;
  private String bootcoinId;
  private String description;
  private BigDecimal amount;
  private LocalDate date;
}
