package com.nttdata.bootcamp.productservice.controller;

import com.nttdata.bootcamp.productservice.application.ProductService;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
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
@RequestMapping("/product")
@RefreshScope
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> update(@Valid @RequestBody ProductDto productDto, @PathVariable String id) {
        return productService.update(id, productDto)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return productService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> findById(@PathVariable String id) {
        return productService.findById(id)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public Flux<ProductDto> findAll() {
        return productService.findAll();
    }
}
