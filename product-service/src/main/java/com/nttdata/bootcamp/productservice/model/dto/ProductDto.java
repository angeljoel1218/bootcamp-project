package com.nttdata.bootcamp.productservice.model.dto;

import com.nttdata.bootcamp.productservice.model.constant.Category;
import com.nttdata.bootcamp.productservice.model.constant.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * javadoc.
 * @since 2022
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
  private String id;

  @NotBlank(message = "code is required")
  private String code;

  @NotBlank(message = "code is required")
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
