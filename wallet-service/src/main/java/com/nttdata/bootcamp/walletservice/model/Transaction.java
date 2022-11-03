package com.nttdata.bootcamp.walletservice.model;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Transaction {

  @Id
  private String id;
  private String phoneOrigin;
  private String phoneDestination;
  private BigDecimal amount;
  private Type type;
  private String description;
  private Date date ;
}
