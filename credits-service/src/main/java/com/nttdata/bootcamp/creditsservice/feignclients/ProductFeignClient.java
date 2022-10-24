package com.nttdata.bootcamp.creditsservice.feignclients;

import com.nttdata.bootcamp.creditsservice.feignclients.fallback.ProductFeignClientFallBack;
import com.nttdata.bootcamp.creditsservice.model.dto.ProductCreditDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value =  "${feign.service.product.name}", fallback = ProductFeignClientFallBack.class)
public interface ProductFeignClient {

    @GetMapping("product-credit/{id}")
    public Mono<ProductCreditDto>  findById (@PathVariable("id") String id);

}
