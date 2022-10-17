package com.nttdata.bootcamp.productservice.controller;

import com.nttdata.bootcamp.productservice.application.ProductCreditService;
import com.nttdata.bootcamp.productservice.model.dto.ProductCreditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class ProductCreditController {
    @Autowired
    ProductCreditService productCreditService;

    @PostMapping("product-credit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductCreditDto> create(@Valid @RequestBody ProductCreditDto productCreditDto) {
        return productCreditService.create(productCreditDto);
    }

    @PutMapping("product-credit/{id}")
    public Mono<ResponseEntity<ProductCreditDto>> update(@Valid @RequestBody ProductCreditDto productCreditDto, @PathVariable String id) {
        return productCreditService.update(id, productCreditDto)
                .map(c -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("product-credit/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return productCreditService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("product-credit/{id}")
    public Mono<ResponseEntity<ProductCreditDto>> findById(@PathVariable String id) {
        return productCreditService.findById(id)
                .map(c -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("product-credit/all")
    public Flux<ProductCreditDto> findAll() {
        return productCreditService.findAll();
    }
}
