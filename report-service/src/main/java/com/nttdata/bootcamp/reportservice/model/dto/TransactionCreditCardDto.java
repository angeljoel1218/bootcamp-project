package com.nttdata.bootcamp.reportservice.model.dto;

import com.nttdata.bootcamp.reportservice.model.constan.TypeTransaction;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */
@Data
public class TransactionCreditCardDto {
    private String id;
    private  String idCredit;
    private String description;
    private BigDecimal amount;
    private TypeTransaction type;
    private Date date;
}
