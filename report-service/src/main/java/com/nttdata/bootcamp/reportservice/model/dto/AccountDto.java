package com.nttdata.bootcamp.reportservice.model.dto;

import com.nttdata.bootcamp.reportservice.model.constan.TypeAccount;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */
@Data
public class AccountDto {
  private String id;
  private String number;
  private BigDecimal balance;
  private String coin;
  private String holderId;
  private String productId;
  private TypeAccount typeAccount;
}
