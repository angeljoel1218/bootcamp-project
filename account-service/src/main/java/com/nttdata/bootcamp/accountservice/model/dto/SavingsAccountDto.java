package com.nttdata.bootcamp.accountservice.model.dto;

import com.nttdata.bootcamp.accountservice.model.constant.StateAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;


@Data
public class SavingsAccountDto extends AccountDto {
  private String id;
  private String number;
  private BigDecimal balance;
  private String coin;
  private String holderId;
  private String productId;
  private Date createdAt;
  private Date updatedAt;
  private StateAccount state;
  private TypeAccount typeAccount;
}
