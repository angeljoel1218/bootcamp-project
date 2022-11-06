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

/**
 * Some javadoc.
 * @since 2022
 */
@RefreshScope
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> update(@Valid @RequestBody ProductDto productDto,
                                                   @PathVariable String id) {
        return productService.update(id, productDto)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return productService.delete(id);
    }

    @GetMapping("/get/{id}")
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
