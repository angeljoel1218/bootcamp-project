package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import reactor.core.publisher.Mono;

public interface OperationService<T> {
    public Mono<String> deposit(OperationDto depositDto);
    public Mono<String> withdraw(OperationDto withdrawDto);
    public Mono<String> wireTransfer(OperationDto withdrawDto);
}
