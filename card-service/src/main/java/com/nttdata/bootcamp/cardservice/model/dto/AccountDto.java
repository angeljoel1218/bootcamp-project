package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private String id;
    private String number;
    private BigDecimal balance;
    private String coin;
}
