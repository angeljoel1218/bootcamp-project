package com.nttdata.bootcamp.creditsservice.feingclients;

import com.nttdata.bootcamp.creditsservice.model.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value =  "${feign.service.customer.name}",   url = "${feign.service.api.gateway}")
public interface CustumerFeingClient {
    @GetMapping("customer/{id}")
    public  Mono<Customer>  findById (@PathVariable("id") String id);

}
