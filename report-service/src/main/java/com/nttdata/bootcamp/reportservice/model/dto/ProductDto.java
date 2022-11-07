package com.nttdata.bootcamp.reportservice.model.dto;

import com.nttdata.bootcamp.reportservice.model.constan.Category;
import com.nttdata.bootcamp.reportservice.model.constan.ProductType;
import lombok.*;

/**
 *
 * @since 2022
 */
@Data
public class ProductDto {

    private String code;
    private String name;
    private Category category;
    private ProductType productTypeId;

}
