package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

/**
 *
 * @since 2022
 */
@Data
public class CardMovementDto {
    private String id;
    private String accountNumber;
    private String detail;
    private String entity;
    private String amount;
    private String operationDate;
    private String transactionId;
}
