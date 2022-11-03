package com.nttdata.bootcamp.wallettransactionservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletDto {
    private String id;
    private String phone;
    private String name;
    private BigDecimal balance;
    private String lastName;
    private String numberDocument;
    private String documentType;
    private String imei;
    private String email;
}
