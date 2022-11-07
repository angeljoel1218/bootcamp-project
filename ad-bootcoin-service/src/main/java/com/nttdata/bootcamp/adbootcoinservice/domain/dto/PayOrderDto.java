package com.nttdata.bootcamp.adbootcoinservice.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */
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
