package com.nttdata.bootcamp.adbootcoinservice.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */

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
    private String advertId;
    private Date createdAt;
}
