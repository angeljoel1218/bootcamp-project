package com.nttdata.bootcamp.creditsservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Document(collection = "credit_card" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
    @Id
    private String id;

    @NotBlank(message = "The field id customer is required")
    private String idCustomer;

    @NotBlank(message = "The field id product is required")
    private  String idProduct;

    @NotNull(message = "the field limit amount is required")
    private BigDecimal limitAmount;

    private BigDecimal amountUsed;

    @NotNull(message = "the field closing day is required")
    private Integer closingDay;

    @NotNull(message = "the field day of pay")
    private Integer dayOfPay;

    private String status;

}
