package com.nttdata.bootcamp.accountservice.client;

import com.nttdata.bootcamp.accountservice.model.dto.Customer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "${feign.service.clients.name}", url = "${feign.service.clients.url}")
public interface CustomerClient {
    @RequestMapping(method = RequestMethod.GET, value = "/customer/{id}")
    Mono<Customer> getClient(@PathVariable("id") String id);
}
