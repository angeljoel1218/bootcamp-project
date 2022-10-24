package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.model.CurrentAccountDto;
import com.nttdata.bootcamp.reportservice.model.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ReportSevice {

    Flux<CurrentAccountDto>  dailyBalance(String customerId);
    Flux<Map<String, Object>>  dailyBalance2(String accountId);

    Mono<CustomerDto> findCustomerById(String id);
}
