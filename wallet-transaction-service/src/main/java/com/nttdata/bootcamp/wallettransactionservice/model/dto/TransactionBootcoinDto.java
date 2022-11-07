package com.nttdata.bootcamp.wallettransactionservice.model.dto;

import com.nttdata.bootcamp.wallettransactionservice.model.constant.StateTransaction;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionBootcoinDto implements Serializable {
    private String sourceNumber;
    private String targetNumber;
    private BigDecimal amount;
    private String transactionId;
    private StateTransaction state;




}
