package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService<T> {
    public Mono<T> create(T accountDto);
    public Mono<T> findByHolderId(String holderId);
    public Mono<T> findByNumber(String number);
    public Flux<TransactionDto> listTransactions(String accountId);
    public Mono<Void> delete(String accountId);
    public Mono<String> deposit(OperationDto depositDto);
    public Mono<String> withdraw(OperationDto withdrawDto);
}
