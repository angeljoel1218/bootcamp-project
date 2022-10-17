package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.StateAccount;
import lombok.Data;

import java.util.Date;

@Data
public class SavingAccountDto {
    private String id;
    private String number;
    private String cci;
    private Float balance;
    private Float interestRate;
    private String coin;
    private String holderId;
    private Date createdAt;
    private Date updatedAt;
    private StateAccount state;
}
