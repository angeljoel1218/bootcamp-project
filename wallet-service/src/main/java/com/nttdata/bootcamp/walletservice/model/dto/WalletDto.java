package com.nttdata.bootcamp.walletservice.model.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */

@Data
public class WalletDto {

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
}
