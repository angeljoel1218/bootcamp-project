package com.nttdata.bootcamp.creditsservice.model;

import com.nttdata.bootcamp.creditsservice.model.constant.TypeTransaction;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "transaction_credit" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionCreditCard {
    @Id
    private String id;

    @NotNull
    private  String idCredit;

    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TypeTransaction type;

    private Date date;

}
