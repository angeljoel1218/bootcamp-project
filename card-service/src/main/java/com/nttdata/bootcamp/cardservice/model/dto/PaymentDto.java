package com.nttdata.bootcamp.cardservice.model.dto;

import com.nttdata.bootcamp.cardservice.model.constant.TypeCredit;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class PaymentDto {
    private String cardNumber;
    private  String idCredit;
    private BigDecimal amount;
    private Integer nroDues;
    private String detail;
    private TypeCredit typeCredit;
}
