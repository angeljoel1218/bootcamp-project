package com.nttdata.bootcamp.reportservice.model.dto;

import com.nttdata.bootcamp.reportservice.model.constan.TypeTransaction;
import com.nttdata.bootcamp.reportservice.model.constan.TypeAccount;
import com.nttdata.bootcamp.reportservice.model.constan.TypeAffectation;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @since 2022
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
  private String id;
  private String operation;
  private BigDecimal amount;
  private BigDecimal commission;
  private String accountId;
  private String sourceAccount;
  private String targetAccount;
  private Date date;
  private TypeTransaction type;
  private TypeAffectation affectation;
  private TypeAccount typeAccount;
}
