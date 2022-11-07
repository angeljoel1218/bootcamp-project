package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigPayment {
    private String sourceNumber;
    private String targetNumber;
}
