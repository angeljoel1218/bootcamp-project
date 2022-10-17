package com.nttdata.bootcamp.accountservice.model.dto;

import lombok.Data;

@Data
public class OperationDto {
    private Float amount;
    private String accountNumber;
    private String operation;
}
