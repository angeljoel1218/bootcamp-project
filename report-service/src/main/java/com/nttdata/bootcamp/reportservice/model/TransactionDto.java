package com.nttdata.bootcamp.reportservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String id;
    private String operation;
    private BigDecimal amount;
    private String accountId;
    private String destination;
    private Date dateOfTransaction;
    private TypeTransaction type;
    private  BigDecimal commission;

}
