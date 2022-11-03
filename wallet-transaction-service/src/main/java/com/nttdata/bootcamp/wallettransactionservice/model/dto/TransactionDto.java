package com.nttdata.bootcamp.wallettransactionservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDto {
    private String sourceWallet;
    private String targetWallet;
    private BigDecimal amount;
    private Date date;
}
