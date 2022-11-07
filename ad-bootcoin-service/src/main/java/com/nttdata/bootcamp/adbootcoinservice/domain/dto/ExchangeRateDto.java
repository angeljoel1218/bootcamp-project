package com.nttdata.bootcamp.adbootcoinservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;


/**
 *
 * @since 2022
 */
@Data
public class ExchangeRateDto {
    private String id;
    private String currency;
    private BigDecimal price;
    private String refCurrency;
}
