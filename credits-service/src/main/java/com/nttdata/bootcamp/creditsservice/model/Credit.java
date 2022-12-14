package com.nttdata.bootcamp.creditsservice.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * javadoc.
 * Bank
 * @since 2022
 */

@Document(collection = "m_credit")
@Data
@AllArgsConstructor
public class Credit {
  @Id
  private String id;

  @NotBlank(message = "The id customer is required")
  private String idCustomer;

  @NotBlank(message = "The Id product is required")
  private  String idProduct;

  @NotNull(message = "Amount credit is required")
  private BigDecimal amountCredit;

  @NotNull(message = "day of pay is required")
  private Integer dayOfPay;

  @NotNull(message = "Nro of dues is required")
  private Integer dues;

  @NotNull(message = "interval dues is required")
  private Integer intervalDues;

  @NotNull(message = "Interest amount is required")
  private BigDecimal interestAmount;


  private String status;
  private BigDecimal amountPayed;
  private Date createDate;

}
