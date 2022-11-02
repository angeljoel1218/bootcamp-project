package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditCardDto {
  private String id;
  private String idCustomer;
  private String idProduct;
  private BigDecimal limitAmount;
  private String status;
}
