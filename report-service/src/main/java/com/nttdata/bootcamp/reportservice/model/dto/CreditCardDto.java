package com.nttdata.bootcamp.reportservice.model.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
public class CreditCardDto {
    private String id;
    private String idCustomer;
    private  String idProduct;
    private BigDecimal limitAmount;
    private BigDecimal amountUsed;
    private Integer closingDay;
    private Integer dayOfPay;
    private String status;

}
