package com.nttdata.bootcamp.adbootcoinservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class RequestPurchaseDto {
    private String advertId;
    private BigDecimal amount;
    private String methodPayment;
    private String buyerWalletId;
}
