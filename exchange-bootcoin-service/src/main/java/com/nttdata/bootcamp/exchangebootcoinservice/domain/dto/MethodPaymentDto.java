package com.nttdata.bootcamp.exchangebootcoinservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MethodPaymentDto {
    @Id
    private String id;
    private String value;
}
