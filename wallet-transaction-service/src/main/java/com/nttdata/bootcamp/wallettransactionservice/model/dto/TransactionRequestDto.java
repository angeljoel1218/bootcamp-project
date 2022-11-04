package com.nttdata.bootcamp.wallettransactionservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto implements Serializable {
    @JsonIgnore
    private String id;
    private String sourceNumberCell;
    private String targetNumberCell;
    private BigDecimal amount;
}
