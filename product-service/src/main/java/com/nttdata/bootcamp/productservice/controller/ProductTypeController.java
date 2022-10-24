package com.nttdata.bootcamp.productservice.controller;

import com.nttdata.bootcamp.productservice.application.ProductTypeService;
import com.nttdata.bootcamp.productservice.model.dto.ProductTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("product/type")
@RefreshScope
public class ProductTypeController {
    @Autowired
    ProductTypeService productTypeService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductTypeDto> create(@Valid @RequestBody ProductTypeDto productTypeDto) {
        return productTypeService.create(productTypeDto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductTypeDto>> update(@Valid @RequestBody ProductTypeDto productTypeDto, @PathVariable String id) {
        return productTypeService.update(id, productTypeDto)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return productTypeService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductTypeDto>> findById(@PathVariable String id) {
        return productTypeService.findById(id)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public Flux<ProductTypeDto> findAll() {
        return productTypeService.findAll();
    }
}
