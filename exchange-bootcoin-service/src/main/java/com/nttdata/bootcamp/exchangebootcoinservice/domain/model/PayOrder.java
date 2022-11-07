package com.nttdata.bootcamp.exchangebootcoinservice.domain.model;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.ConfigPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "pay_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayOrder {
    @Id
    private String id;
    private BigDecimal amount;
    private BigDecimal amountPay;
    private BigDecimal exchangeRate;
    private String sellerWalletId;
    private String buyerWalletId;
    private String methodPayment;
    private ConfigPayment configPayment;
    private Date createdAt;
}
