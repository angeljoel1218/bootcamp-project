package com.nttdata.bootcamp.productservice.model.dto;

import com.nttdata.bootcamp.productservice.model.constant.Category;
import com.nttdata.bootcamp.productservice.model.constant.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * javadoc.
 * ProductDto
 * @since 2022
 */

@AllArgsConstructor
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
  private ProductType productTypeId;
}
