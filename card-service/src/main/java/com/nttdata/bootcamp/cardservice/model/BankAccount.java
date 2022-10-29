package com.nttdata.bootcamp.cardservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class BankAccount {
    private String accountNumber;
    private Boolean isDefault;
    private Integer order;
    private Date createdAt;
}
