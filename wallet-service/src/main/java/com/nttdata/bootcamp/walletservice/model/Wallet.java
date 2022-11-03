package com.nttdata.bootcamp.walletservice.model;

import com.nttdata.bootcamp.walletservice.model.dto.CardDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Wallet {

  @Id
  private String id;
  private String phone;
  private String name;
  private BigDecimal balance;
  private String lastName;
  private String numberDocument;
  private String documentType;
  private String imei;
  private String email;
  private CardDto card;
  private LocalDate createAt;

}
