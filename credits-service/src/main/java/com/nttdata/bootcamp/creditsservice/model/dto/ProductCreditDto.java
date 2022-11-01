package com.nttdata.bootcamp.creditsservice.model.dto;

import com.nttdata.bootcamp.creditsservice.model.constant.Category;
import com.nttdata.bootcamp.creditsservice.model.constant.ProductType;
import lombok.Data;


/**
 * javadoc.
 * Bank
 * @since 2022
 */

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
  private ProductType productTypeId;

}
