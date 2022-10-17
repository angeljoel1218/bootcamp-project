package com.nttdata.bootcamp.creditsservice.feingclients;

import com.nttdata.bootcamp.creditsservice.model.ProductCredit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value =  "${feign.service.product.name}",   url = "${feign.service.product.url}")
public interface ProductFeingClient {

    @GetMapping("product-credit/{id}")
    public Mono<ProductCredit>  findById (@PathVariable("id") String id);

}
