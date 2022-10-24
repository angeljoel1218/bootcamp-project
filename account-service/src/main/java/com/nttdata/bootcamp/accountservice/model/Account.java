package com.nttdata.bootcamp.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String number;
    private BigDecimal balance;
    private String coin;
    private String holderId;
    private String productId;
    private StateAccount state;
    private TypeAccount typeAccount;
}
