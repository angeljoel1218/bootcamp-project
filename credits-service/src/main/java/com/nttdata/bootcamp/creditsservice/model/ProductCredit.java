package com.nttdata.bootcamp.creditsservice.model;

import com.nttdata.bootcamp.creditsservice.model.TypeCredit;
import lombok.Data;

@Data
public class ProductCredit {
    private String id;
    private String code;
    private String name;
    private Integer maxNumber;
    private String coin;
    private TypeCredit type;
}
