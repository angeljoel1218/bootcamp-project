package com.nttdata.bootcamp.exchangebootcoinservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_payment_method")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigPaymentMethod {
    @Id
    private String id;
    private String methodPaymentId;
    private String numberAccount;
    private String numberCellPhone;
    private String walletId;
}
