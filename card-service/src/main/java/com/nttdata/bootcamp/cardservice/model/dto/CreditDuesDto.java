package com.nttdata.bootcamp.cardservice.model.dto;

import com.nttdata.bootcamp.cardservice.model.constant.CreditStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */
@Data
public class CreditDuesDto {
    private String id;
    private String idCredit;
    private Integer nroDues;
    private BigDecimal amount;
    private BigDecimal interest;
    private BigDecimal totalAmount;
    private Date expirationDate;
    private Date paidDate;
    private CreditStatus status;
}
