package com.nttdata.bootcamp.creditsservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Document(collection = "m_credit" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Credit {

    @Id
    private String id;

    @NotBlank(message = "Id del cliente es requerido")
    private String idCustomer;

    @NotBlank(message = "Id del producto es requerido")
    private  String idProduct;

    private BigDecimal amount;

    private  Integer dues;

    private String status;

}
