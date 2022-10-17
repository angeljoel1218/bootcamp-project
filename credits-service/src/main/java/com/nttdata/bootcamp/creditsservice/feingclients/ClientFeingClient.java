package com.nttdata.bootcamp.creditsservice.feingclients;

import com.nttdata.bootcamp.creditsservice.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value =  "clients-service",   url = "http://localhost:9040")
public interface ClientFeingClient {
    @GetMapping("client/{id}")
    public  Mono<Object>  findById (@PathVariable("id") String id);

}
