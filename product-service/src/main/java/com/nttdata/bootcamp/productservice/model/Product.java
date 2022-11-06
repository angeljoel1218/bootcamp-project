package com.nttdata.bootcamp.productservice.model;

import com.nttdata.bootcamp.productservice.model.constant.Category;
import com.nttdata.bootcamp.productservice.model.constant.ProductType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Some javadoc.
 * @since 2022
 */
@Document
@Builder
@Data
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
    private ProductType productTypeId;
}
