package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayOrderDto {
    private String id;
    private BigDecimal amount;
    private BigDecimal amountPay;
    private BigDecimal exchangeRate;
    private String sellerWalletId;
    private String buyerWalletId;
    private String methodPayment;
    private Date createdAt;
}
