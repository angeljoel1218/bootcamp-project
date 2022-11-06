package com.nttdata.bootcamp.adbootcoinservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestPurchaseDto {
    private String advertId;
    private BigDecimal amount;
    private String methodPayment;
    private String buyerWalletId;
}
