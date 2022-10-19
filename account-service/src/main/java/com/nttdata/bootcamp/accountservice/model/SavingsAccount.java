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
public class SavingsAccount {
    @Id
    private String id;
    private String number;
    private Float balance;
    private String coin;
    private String holderId;
    private String productId;
    private Date createdAt;
    private Date updatedAt;
    private StateAccount state;
}
