package com.nttdata.bootcamp.accountservice.feignclient;

import com.nttdata.bootcamp.accountservice.model.dto.Customer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "${feign.service.credit.name}", url = "${feign.service.credit.url}")
public interface CreditClient {
    @RequestMapping(method = RequestMethod.GET, value = "/credit/{id}")
    Mono<Customer> getCreditCard(@PathVariable("id") String id);
}
