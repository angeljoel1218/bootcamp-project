package com.nttdata.bootcamp.masterdatabootcoinservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    @Id
    private String id;
    private String currency;
    private BigDecimal price;
    private String refCurrency;
}
