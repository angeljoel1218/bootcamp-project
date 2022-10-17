package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.TypeAccount;
import lombok.Data;

@Data
public class ProductAccountDto {
    private String id;

    private String code;

    private String name;

    private Float maintenance;

    private Integer maxMovements;

    private String coin;

    private TypeAccount type;
}
