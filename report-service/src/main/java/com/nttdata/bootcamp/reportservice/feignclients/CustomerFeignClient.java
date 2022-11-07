package com.nttdata.bootcamp.reportservice.feignclients;

import com.nttdata.bootcamp.reportservice.feignclients.fallback.CustomerFeignClientFallBack;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;


/**
 *
 * @since 2022
 */
@ReactiveFeignClient(value =  "${feign.service.customer.name}",
  fallback = CustomerFeignClientFallBack.class)
public interface CustomerFeignClient {
  @GetMapping("customer/{id}")
  public  Mono<CustomerDto> findCustomerById(@PathVariable("id") String id);
}
