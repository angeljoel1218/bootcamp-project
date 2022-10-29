package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.model.dto.AccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import reactor.core.publisher.Flux;

public interface GeneralAccountService {

  Flux<AccountDto> findByHolderId(String holderId);

  Flux<TransactionDto> findTransactionByAccountId(String accountId);
}
