package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.GeneralAccountService;
import com.nttdata.bootcamp.accountservice.application.ManyAccountService;
import com.nttdata.bootcamp.accountservice.model.dto.AccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RefreshScope
@RestController
public class AccountController {

  @Autowired
  GeneralAccountService generalAccountService;

  @GetMapping("account/customer/{holderId}")
  public Flux<AccountDto> findByHolderId(@PathVariable("holderId") String holderId){
    return generalAccountService.findByHolderId(holderId);
  }

  @GetMapping("account/transaction/{accountId}")
  public Flux<TransactionDto> findTransactionByAccountId(@PathVariable("accountId") String accountId){
    return generalAccountService.findTransactionByAccountId(accountId);
  }

}
