package com.nttdata.bootcamp.productservice.model.dto;

import com.nttdata.bootcamp.productservice.model.TypeAccount;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ProductAccountDto {
    private String id;

    @NotEmpty(message = "The field code is required")
    private String code;

    @NotEmpty(message = "The field name is required")
    private String name;

    private Float maintenance;

    private Integer maxMovements;

    @NotEmpty(message = "The field coin is required")
    private String coin;

    private TypeAccount type;
}
