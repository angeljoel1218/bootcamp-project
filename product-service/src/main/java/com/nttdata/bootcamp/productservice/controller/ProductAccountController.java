package com.nttdata.bootcamp.productservice.controller;

import com.nttdata.bootcamp.productservice.application.ProductAccountService;
import com.nttdata.bootcamp.productservice.model.TypeAccount;
import com.nttdata.bootcamp.productservice.model.dto.ProductAccountDto;
import com.nttdata.bootcamp.productservice.application.mappers.MapperProductAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class ProductAccountController {
    @Autowired
    ProductAccountService productAccountService;

    @PostMapping("product-account")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductAccountDto> create(@Valid @RequestBody ProductAccountDto productAccountDto) {
        return productAccountService.create(productAccountDto);
    }

    @PutMapping("product-account/{id}")
    public Mono<ResponseEntity<ProductAccountDto>> update(@Valid @RequestBody ProductAccountDto productAccountDto, @PathVariable String id) {
        return productAccountService.update(id, productAccountDto)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("product-account/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return productAccountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("product-account/{id}")
    public Mono<ResponseEntity<ProductAccountDto>> findById(@PathVariable String id) {
        return productAccountService.findById(id)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("product-account/all")
    public Flux<ProductAccountDto> findAll() {
        return productAccountService.findAll();
    }
}
