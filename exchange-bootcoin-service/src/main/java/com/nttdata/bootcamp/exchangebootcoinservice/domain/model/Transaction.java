package com.nttdata.bootcamp.exchangebootcoinservice.domain.model;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.constant.StateTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String orderId;
    private String number;
    private String detail;
    private String sellerWalletId;
    private String buyerWalletId;
    private StateTransaction state;
    private Date dateTransaction;
    private Date createdAt;
}
