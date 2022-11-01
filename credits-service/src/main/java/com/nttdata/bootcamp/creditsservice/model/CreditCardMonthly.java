package com.nttdata.bootcamp.creditsservice.model;

import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import java.math.BigDecimal;
import java.util.Date;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * javadoc.
 * Bank
 * @since 2022
 */

@Document(collection = "credit_card_monthly")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditCardMonthly {

  @Id
  private String id;

  private String idCreditCard;

  private Date startDate;

  private Date endDate;

  private BigDecimal amount;

  private Date expireDate;

  private Date paidDate;

  private CreditStatus status;

}
