package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.model.ProductConsumer;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

/**
 *
 * @since 2022
 */
public interface ReportService {

  Flux<Map<String, Object>>  dailyBalance(String customerId);

  Flux<Mono<Map<String, Object>>>  productsCommissionByDates(Date startData,
                                                             Date endDate, String customerId);

  Mono<CustomerDto> findCustomerById(String id);

  Flux<Map<String, Object>>  findProductsByCustomer(String customerId);

  Mono<ProductConsumer> findProductConsumerByDateBetween(String idProduct,
                                                         Date startData, Date endDate);


}
