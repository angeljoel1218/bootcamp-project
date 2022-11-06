package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import lombok.Data;

@Data
public class ConfigPaymentMethodDto {
    private String id;
    private String methodPaymentId;
    private String numberAccount;
    private String numberCellPhone;
    private String walletId;
}
