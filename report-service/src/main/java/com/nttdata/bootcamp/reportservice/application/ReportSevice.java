package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

public interface ReportSevice {

    Flux<Map<String,Object>>  dailyBalance(String customerId);

    Flux<Map<String,Object>>  productsCommissionByDates(Date startData, Date endDate, String customerId);

    Mono<CustomerDto> findCustomerById(String id);
}
