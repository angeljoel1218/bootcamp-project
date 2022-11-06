package com.nttdata.bootcamp.customerservice.aplication;

import com.nttdata.bootcamp.customerservice.aplication.mappers.MapperCustomer;
import com.nttdata.bootcamp.customerservice.infraestructure.CustomerRepository;
import com.nttdata.bootcamp.customerservice.model.Customer;
import com.nttdata.bootcamp.customerservice.model.constants.Constants;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  MapperCustomer mapperCustomer;
  @Autowired
  CustomerRepository costumerRepository;

  @Override
  public Mono<CustomerDto> create(CustomerDto  customerDto) {
    return Mono.just(customerDto).map(mapperCustomer::toCustomer)
      .map(t -> changeStatus(t, Constants.STATUS_CREATED)).flatMap(costumerRepository::insert)
      .map(mapperCustomer::toDto);
  }

  @Override
  public Mono<CustomerDto> update(CustomerDto customerDto, String id) {
    return costumerRepository.findById(id)
      .switchIfEmpty(Mono.error(new RuntimeException("this product is not personal credit")))
      .flatMap(c -> Mono.just(customerDto).map(mapperCustomer::toCustomer))
      .doOnNext(c -> c.setId(id))
      .map(t -> changeStatus(t, Constants.STATUS_UPDATED))
      .flatMap(costumerRepository::save)
      .map(mapperCustomer::toDto);

  }

  @Override
  public Mono<Void> delete(String id) {
    return  costumerRepository.findById(id).flatMap(costumerRepository::delete);
  }

  @Override
  public Flux<CustomerDto> findAll() {
    return costumerRepository.findAll().map(mapperCustomer::toDto);
  }

  @Override
  public Mono<CustomerDto> findById(String id) {
    return costumerRepository.findById(id).map(mapperCustomer::toDto);
  }

  private Customer changeStatus(Customer customer, String status) {
    customer.setStatus(status);
    return  customer;
  }
}
