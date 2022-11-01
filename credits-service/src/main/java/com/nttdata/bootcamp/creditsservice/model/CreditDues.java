package com.nttdata.bootcamp.creditsservice.model;

import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * javadoc.
 * Bank
 * @since 2022
 */
@Document(collection = "credit_dues")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditDues {
  @Id
  private String id;

  @NotNull(message = "credit is required")
  private String idCredit;

  @NotNull(message = "nro dues is required")
  private Integer nroDues;

  @NotNull(message = "amount dues is required")
  private BigDecimal amount;

  @NotNull(message = "interest dues is required")
  private BigDecimal interest;

  @NotNull(message = "interest dues is required")
  private BigDecimal totalAmount;

  @NotNull(message = "expiration date is required")
  private Date expirationDate;

  private Date paidDate;

  @NotNull(message = "expiration date is required")
  private CreditStatus status;


}
