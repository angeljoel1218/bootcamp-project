package com.nttdata.bootcamp.creditsservice.model;

import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "payment_credit" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentCredit {
    @Id
    private String id;

    @NotNull
    private  String idCredit;

    @NotNull
    private BigDecimal amount;


    private Date date;


}
