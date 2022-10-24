package com.nttdata.bootcamp.creditsservice.feignclients;

import com.nttdata.bootcamp.creditsservice.model.dto.CustomerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value =  "${feign.service.customer.name}")
public interface CustomerFeignClient {
    @GetMapping("customer/{id}")
    public  Mono<CustomerDto>  findById (@PathVariable("id") String id);

}
