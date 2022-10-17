package com.nttdata.bootcamp.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String operation;
    private Float amount;
    private String origin;
    private String destination;
    private Date dateOfTransaction;
    private TypeTransaction type;
}
