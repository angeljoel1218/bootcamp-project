package com.nttdata.bootcamp.productservice.controller;

import com.nttdata.bootcamp.productservice.application.ProductService;
import com.nttdata.bootcamp.productservice.model.constant.Category;
import com.nttdata.bootcamp.productservice.model.constant.ProductType;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
class ProductControllerTests {

    @Autowired
    private WebTestClient webTestClient;


    @MockBean
    private ProductService productService;


    @Test
    void create() {
        var monoDto = new ProductDto(null,"PRO001","Prodcuto de crédito",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT);

        when(productService.create(monoDto)).thenReturn(Mono.just(monoDto));

        webTestClient.post().uri("/product/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(monoDto),ProductDto.class)
            .exchange()
            .expectStatus().isCreated();
    }

    @Test
    void createInvalidData() {

      webTestClient.post().uri("/product/create")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(new ProductDto()),ProductDto.class)
        .exchange()
        .expectStatus().is5xxServerError();

    }

    @Test
    void update() {
        var id = "id323232323232";
        var monoDto = new ProductDto("id323232323232","PRO001","Prodcuto de crédito",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT);

        when(productService.update(id,monoDto)).thenReturn(Mono.just(monoDto));

        webTestClient.put().uri("/product/"+id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(monoDto),ProductDto.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void delete() {
        var id = "id323232323232";
        when(productService.delete(id)).thenReturn(Mono.empty().then());
        webTestClient.delete().uri("/product/"+id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void findById() {
        var monoDto = new ProductDto("70150585sdsds","PRO001","Prodcuto de crédito",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT);

        when(productService.findById("70150585sdsds")).thenReturn(Mono.just(monoDto));

        var responseFlux= webTestClient.get().uri("/product/get/70150585sdsds")
            .exchange()
            .expectStatus().isOk()
            .returnResult(ProductDto.class)
            .getResponseBody();



        StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext( new ProductDto("70150585sdsds","PRO001","Prodcuto de crédito",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT)
            )
            .verifyComplete();
    }

    @Test
    void findAll() {

        var empFlux = Flux.just(
            new ProductDto("70150585sdsds","PRO001","Prodcuto de crédito",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT),
            new ProductDto("e3234dfsdfsrw","PRO002","Prodcuto de debido",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT)
        );

        when(productService.findAll()).thenReturn(empFlux);

        var responseFlux = webTestClient.get().uri("/product/all")
            .exchange()
            .expectStatus().isOk()
            .returnResult(ProductDto.class)
            .getResponseBody();

        StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext(new ProductDto("70150585sdsds","PRO001","Prodcuto de crédito",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT))
            .expectNext(   new ProductDto("e3234dfsdfsrw","PRO002","Prodcuto de debido",Float.valueOf("1"),20,10,Float.valueOf("10"),Float.valueOf("10"),Float.valueOf("10"), Category.ACTIVE, ProductType.BUSINESS_CREDIT))
            .verifyComplete();


    }


}
