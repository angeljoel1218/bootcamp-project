package com.nttdata.bootcamp.creditsservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Document(collection = "credit_card" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
    @Id
    private String id;

    @NotBlank(message = "Id Cliente es requerido")
    private String idCustumer;

    @NotBlank(message = "Id del producto es requerido")
    private  String idProduct;

    @NotNull(message = "Monto limite es requerido")
    private BigDecimal limitAmount;

    private String status;

}
