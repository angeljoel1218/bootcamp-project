package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.constant.TypeTransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDto {
    private String id;
    private String operation;
    private BigDecimal amount;
    private String origin;
    private String destination;
    private Date dateOfTransaction;
    private TypeTransaction type;
    private TypeAffectation affectation;
    private TypeAccount typeAccount;
}
