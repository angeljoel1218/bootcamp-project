package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDto {
    private String accountNumber;
    private Boolean isDefault;
    private Integer order;
    private Date createdAt;
}
