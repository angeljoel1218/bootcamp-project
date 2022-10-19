package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.StateAccount;
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
    private StateAccount state;
}
