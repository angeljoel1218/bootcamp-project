package com.nttdata.bootcamp.cardservice.model.dto;

import lombok.Data;

@Data
public class AttachAccountDto {
    private String cardNumber;
    private String accountNumber;
}
