package com.nttdata.bootcamp.customerservice.controller;

import com.nttdata.bootcamp.customerservice.aplication.CustomerService;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/customer")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public  Mono<CustomerDto> create(@RequestBody @Valid CustomerDto customerDto) {
    return customerService.create(customerDto);
  }

  @PutMapping("/{id}")
  public  Mono<CustomerDto> update(@RequestBody @Valid CustomerDto customerDto,
                   @PathVariable String id) {
    return customerService.update(customerDto, id);
  }

  @DeleteMapping("/delete/{id}")
  public  Mono<Void> delete(@PathVariable String id) {
    return customerService.delete(id);
  }

  @GetMapping("/all")
  public Flux<CustomerDto> findAll() {
    return customerService.findAll();
  }

  @GetMapping("/get/{id}")
  public Mono<ResponseEntity<CustomerDto>> findById(@PathVariable("id") String id) {
    return customerService.findById(id)
      .map(a -> ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

}
