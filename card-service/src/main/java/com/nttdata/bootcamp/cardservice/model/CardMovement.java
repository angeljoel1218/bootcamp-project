package com.nttdata.bootcamp.cardservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("card_movements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardMovement {
    @Id
    private String id;
    private String cardId;
    private String accountNumber;
    private String detail;
    private String entity;
    private BigDecimal amount;
    private String operationDate;
    private String transactionId;
}
