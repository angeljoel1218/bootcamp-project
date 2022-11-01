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
@Document(collection = "credit_card")
@AllArgsConstructor
@Data
public class CreditCard {
  @Id
  private String id;

  @NotBlank(message = "The field id customer is required")
  private String idCustomer;

  @NotBlank(message = "The field id product is required")
  private  String idProduct;

  @NotNull(message = "the field limit amount is required")
  private BigDecimal limitAmount;

  private BigDecimal amountUsed;

  @NotNull(message = "the field closing day is required")
  private Integer closingDay;

  @NotNull(message = "the field day of pay")
  private Integer dayOfPay;

  private String status;

  private Date createDate;

}
