package com.nttdata.bootcamp.creditsservice.model;

import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @since 2022
 */
@Document(collection = "credit_dues" )
@Getter
@Setter
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

    // Start
    @NotNull(message = "expiration date is required")
    private CreditStatus status;


}
