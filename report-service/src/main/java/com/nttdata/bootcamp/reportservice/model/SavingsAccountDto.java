package com.nttdata.bootcamp.reportservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SavingsAccountDto {
    private String id;
    private String number;
    private Float balance;
    private String coin;
    private String holderId;
    private String productId;
    private Date createdAt;
    private Date updatedAt;
}
