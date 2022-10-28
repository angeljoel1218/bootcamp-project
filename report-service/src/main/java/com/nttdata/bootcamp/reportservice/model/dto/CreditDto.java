package com.nttdata.bootcamp.reportservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditDto {

  private String id;
  private String idCustomer;
  private String idProduct;
  private BigDecimal amountCredit;
  private Integer dayOfPay;
  private Integer dues;
  private Integer intervalDues;
  private BigDecimal interestAmount;

}
