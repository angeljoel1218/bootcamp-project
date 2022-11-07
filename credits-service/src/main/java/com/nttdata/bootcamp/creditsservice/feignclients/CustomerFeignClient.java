package com.nttdata.bootcamp.creditsservice.feignclients;
import com.nttdata.bootcamp.creditsservice.feignclients.fallback.CustomerFeignClientFallBack;
import com.nttdata.bootcamp.creditsservice.model.dto.CustomerDto;
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
  @GetMapping("customer/get/{id}")
  public Mono<CustomerDto> findById(@PathVariable("id") String id);
}