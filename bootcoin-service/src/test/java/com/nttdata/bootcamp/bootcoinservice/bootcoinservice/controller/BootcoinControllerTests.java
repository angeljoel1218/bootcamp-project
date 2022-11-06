package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.controller;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.BootcoinService;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.controller.BootcoinController;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.MovementsDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(BootcoinController.class)
class BootcoinControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BootcoinService bootcoinService;


    @Test
    void create() {
      var dto= BootcoinDto.builder()
        .numberDocument("70150525")
        .documentType("DNI").phone("986434107")
        .names("ALEXNADER BEJARANO")
        .email("bejarano@gmail.com")
        .build();

        when(bootcoinService.create(dto)).thenReturn(Mono.just(dto));

        webTestClient.post().uri("/bootcoin/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(dto),BootcoinDto.class)
            .exchange()
            .expectStatus().isCreated();
    }

    @Test
    void createInvalidData() {
      webTestClient.post().uri("/product/create")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(new BootcoinDto()),BootcoinDto.class)
        .exchange()
        .expectStatus().is4xxClientError();
    }


    @Test
    void findByPhone() {
      var dto= BootcoinDto.builder()
        .numberDocument("70150525")
        .documentType("DNI").phone("986434107")
        .names("ALEXNADER BEJARANO")
        .email("bejarano@gmail.com")
        .build();


      when(bootcoinService.findByPhone("986434107")).thenReturn(Mono.just(dto));

      var responseFlux= webTestClient.get().uri("/bootcoin/get/986434107")
            .exchange()
            .expectStatus().isOk()
            .returnResult(BootcoinDto.class)
            .getResponseBody();


      StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext(dto)
            .verifyComplete();
    }

    @Test
    void addMovements() {

      var movement = MovementsDto.builder()
        .phone("986434107")
        .description("movement")
        .amount(BigDecimal.TEN)
        .date(LocalDate.now()).build();

      when(bootcoinService.addMovements(movement)).thenReturn(
        Mono.just(BootcoinDto.builder()
          .numberDocument("70150525")
          .documentType("DNI").phone("986434107")
          .names("ALEXNADER BEJARANO")
          .email("bejarano@gmail.com")
          .balance(BigDecimal.TEN)
          .build()));


      webTestClient.post().uri ("/bootcoin/transaction")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(movement),MovementsDto.class)
        .exchange()
        .expectStatus().isCreated();

    }


  @Test
  void findAll() {
    var dtoOne= BootcoinDto.builder()
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .build();

    var dtoTwo= BootcoinDto.builder()
      .numberDocument("787484")
      .documentType("DNI").phone("78747477")
      .names("Angel alvarado cristano")
      .email("maria@bejarano.com")
      .build();

    when(bootcoinService.findAll()).thenReturn(Flux.just(dtoOne, dtoTwo));

    var responseFlux= webTestClient.get().uri("/bootcoin/get/all")
      .exchange()
      .expectStatus().isOk()
      .returnResult(BootcoinDto.class)
      .getResponseBody();


    StepVerifier.create(responseFlux)
      .expectSubscription()
      .expectNext(dtoOne)
      .expectNext(dtoTwo)
      .verifyComplete();
  }


}
