package com.nttdata.bootcamp.reportservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class CurrentAccountDto {
    private String id;
    private String number;
    private Float balance;
    private String coin;
    private String holderId;
    private String [] holders;
    private String [] authorizedSigners;
    private String productId;
    private Date createdAt;
    private Date updatedAt;
}
