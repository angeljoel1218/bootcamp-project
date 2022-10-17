package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.StateAccount;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentAccountDto {
    private String id;
    private String number;
    private String cci;
    private Float balance;
    private String coin;
    private String holderId;
    private String [] holders;
    private String [] authorizedSigners;
    private Date createdAt;
    private Date updatedAt;
    private StateAccount state;
}
