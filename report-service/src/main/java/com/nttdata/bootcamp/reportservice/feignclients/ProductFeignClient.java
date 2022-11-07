package com.nttdata.bootcamp.reportservice.feignclients;

import com.nttdata.bootcamp.reportservice.feignclients.fallback.ProductFeignClientFallBack;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
@ReactiveFeignClient(value =  "${feign.service.product.name}",
  fallback = ProductFeignClientFallBack.class)
public interface ProductFeignClient {

  @GetMapping("product/get/{id}")
  public  Mono<ProductDto> findById(@PathVariable("id") String id);
}
