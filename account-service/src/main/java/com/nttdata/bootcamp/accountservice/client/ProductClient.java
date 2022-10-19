package com.nttdata.bootcamp.accountservice.client;

import com.nttdata.bootcamp.accountservice.model.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.dto.ProductAccountDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "${feign.service.product.name}", url = "${feign.service.product.url}")
public interface ProductClient {
    @RequestMapping(method = RequestMethod.GET, value = "/product-account/{id}")
    Mono<ProductAccountDto> getProductAccount(@PathVariable("id") String id);
}
