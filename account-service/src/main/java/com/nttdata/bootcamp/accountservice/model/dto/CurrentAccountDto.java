package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.StateAccount;
import com.nttdata.bootcamp.accountservice.model.TypeAccount;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CurrentAccountDto {
    private String id;
    private String number;
    private BigDecimal balance;
    private String coin;
    private String holderId;
    private String [] holders;
    private String [] authorizedSigners;
    private String productId;
    private Date createdAt;
    private Date updatedAt;
    private StateAccount state;
    private TypeAccount typeAccount;
}
