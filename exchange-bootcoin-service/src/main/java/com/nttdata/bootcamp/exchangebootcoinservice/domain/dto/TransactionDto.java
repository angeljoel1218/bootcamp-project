package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.constant.StateTransaction;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {
    private String id;
    private String orderId;
    private String number;
    private String detail;
    private StateTransaction state;
    private Date dateTransaction;
    private Date createdAt;
}
