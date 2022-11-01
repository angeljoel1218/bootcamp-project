package com.nttdata.bootcamp.customerservice.aplication;

import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */
public interface CustomerService {

  Mono<CustomerDto> create(CustomerDto customerDto);

  Mono<CustomerDto> update(CustomerDto customerDto, String id);

  Mono<Void> delete(String id);

  Mono<CustomerDto> findById(String id);

  Flux<CustomerDto> findAll();

}
