package com.nttdata.bootcamp.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCredit {
    @Id
    private String id;
    private String code;
    private String name;
    private Integer maxNumber;
    private String coin;
    private TypeCredit type;
}
