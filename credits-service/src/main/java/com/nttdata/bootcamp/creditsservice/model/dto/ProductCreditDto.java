package com.nttdata.bootcamp.creditsservice.model.dto;

import com.nttdata.bootcamp.creditsservice.model.constant.Category;
import lombok.Data;

@Data
public class ProductCreditDto {
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
