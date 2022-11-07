package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class AccountDto {
    private String id;
    private String number;
    private BigDecimal balance;
    private String coin;
}
