package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CreditController {
    @Autowired
    CreditService creditService;

    @PostMapping("credit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Credit> create(@RequestBody Credit credit) {
        return creditService.create(Mono.just(credit));
    }

    @PutMapping("credit/{id}")
    public Mono<Credit> update(@RequestBody Credit credit, @PathVariable String id) {
        return creditService.update(id, credit);
    }

    @DeleteMapping("credit/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return creditService.delete(id);
    }

    @GetMapping("credit/{id}")
    public Mono<Credit> findById(@PathVariable String id) {
        return creditService.findById(id);
    }

    @GetMapping("credit/all")
    public Flux<Credit> findAll() {
        return creditService.findAll();
    }
}
