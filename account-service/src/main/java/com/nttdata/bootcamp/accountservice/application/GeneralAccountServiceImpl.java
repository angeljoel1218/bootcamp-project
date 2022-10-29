package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mapper.MapperAccount;
import com.nttdata.bootcamp.accountservice.application.mapper.MapperTransaction;
import com.nttdata.bootcamp.accountservice.infrastructure.AccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.dto.AccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

public class GeneralAccountServiceImpl implements GeneralAccountService{

  @Autowired
  AccountRepository accountRepository;


  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  MapperAccount mapperAccount;

  @Autowired
  MapperTransaction mapperTransaction;

  @Override
  public Flux<AccountDto> findByHolderId(String holderId) {
    return accountRepository.findByHolderId(holderId).flatMap(mapperAccount::toDto);
  }

  @Override
  public Flux<TransactionDto> findTransactionByAccountId(String accountId) {
    return transactionRepository.findByAccountId(accountId).flatMap(mapperTransaction::toDto);
  }
}
