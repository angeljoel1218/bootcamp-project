package com.nttdata.bootcamp.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    private String code;
    private String name;
    private Float maintenance;
    private Integer maxMovements;
    private Integer maxNumberCredits;
    private Float commissionAmount;
    private Float openingAmount;
    private Float minFixedAmount;
    private Category category;
    private String productTypeId;
}
