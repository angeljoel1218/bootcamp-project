package com.nttdata.bootcamp.reportservice.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreditCardDto {

    private String id;
    private String idCustomer;
    private String idProduct;
    private BigDecimal limitAmount;
}
