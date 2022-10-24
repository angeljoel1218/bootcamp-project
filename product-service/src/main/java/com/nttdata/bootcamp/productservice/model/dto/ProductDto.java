package com.nttdata.bootcamp.productservice.model.dto;

import com.nttdata.bootcamp.productservice.model.Category;
import lombok.Data;

@Data
public class ProductDto {
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
