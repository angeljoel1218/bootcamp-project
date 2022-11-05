package com.nttdata.bootcamp.wallettransactionservice.model.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto implements Serializable {
    private String sourceNumberCell;
    private String targetNumberCell;
    private BigDecimal amount;
}
