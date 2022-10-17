package com.nttdata.bootcamp.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductAccount {
    @Id
    private String id;
    private String code;
    private String name;
    private Float maintenance;
    private Integer maxMovements;
    private String coin;
    private TypeAccount type;
}
