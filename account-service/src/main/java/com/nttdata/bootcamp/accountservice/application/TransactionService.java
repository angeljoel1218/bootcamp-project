package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.model.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService<T> {
    public Mono<TransactionDto> deposit(DepositDto depositDto);
    public Mono<TransactionDto> withdraw(WithdrawDto withdrawDto);
    public Mono<TransactionDto> wireTransfer(TransactionRequestDto transferDto);
    public Flux<TransactionDto> listTransactions(String accountId);
}
