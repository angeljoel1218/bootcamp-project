package com.nttdata.bootcamp.creditsservice.model;


import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */
@Document(collection = "credit_card_monthly" )
@Getter
@Setter
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
