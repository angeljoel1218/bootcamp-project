package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationDto {
    private BigDecimal amount;
    private BigDecimal commission;
    private String origAccountNumber;
    private String destAccountNumber;
    private String accountId;
    private String operation;
    private String originId;
    private String destinationId;
    private String holderId;
}
