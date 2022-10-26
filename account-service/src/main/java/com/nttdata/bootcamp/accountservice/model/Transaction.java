package com.nttdata.bootcamp.accountservice.model;

import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.constant.TypeTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
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
    private BigDecimal amount;
    private BigDecimal commission;
    private String accountId;
    private String origin;
    private String destination;
    private Date dateOfTransaction;
    private TypeTransaction type;
    private TypeAffectation affectation;
    private TypeAccount typeAccount;
}
