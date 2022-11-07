package com.nttdata.bootcamp.reportservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class CreditDto {
  private String id;
  private String idCustomer;
  private  String idProduct;
  private BigDecimal amountCredit;
  private Integer dayOfPay;
  private Integer dues;
  private Integer intervalDues;
  private BigDecimal interestAmount;
  private BigDecimal amountPayed;

}
