package com.nttdata.bootcamp.accountservice.client;

import com.nttdata.bootcamp.accountservice.model.dto.Client;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "${feign.service.clients.name}", url = "${feign.service.product.url}")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/client/{id}")
    Mono<Client> getClient(@PathVariable("id") String id);
}
