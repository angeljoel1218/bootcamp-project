package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.TypeTransaction;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {
    private String id;
    private String operation;
    private Float amount;
    private String origin;
    private String destination;
    private Date dateOfTransaction;
    private TypeTransaction type;
}
