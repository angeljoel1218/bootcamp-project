package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.constant.StateAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import lombok.Data;

import java.math.BigDecimal;

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
