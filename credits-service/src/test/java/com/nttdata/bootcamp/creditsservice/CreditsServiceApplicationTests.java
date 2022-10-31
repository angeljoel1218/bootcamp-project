package com.nttdata.bootcamp.creditsservice;

import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.application.utils.DateUtil;
import com.nttdata.bootcamp.creditsservice.controller.CreditController;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CreditController.class)
class CreditsServiceApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreditService creditService;


    @Test
	void create() {

        var monoDto = new Credit(null,"CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate());

        when(creditService.create(monoDto)).thenReturn(Mono.just(monoDto));

        webTestClient.post().uri("/credit")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(monoDto),Credit.class)
            .exchange()
            .expectStatus().isCreated();
	}

    @Test
    void payment() {
        var monoDto = new CreditDuesDto();
        monoDto.setIdCredit("ID12121212");
        monoDto.setNroDues(1);
        monoDto.setAmount(BigDecimal.valueOf(100));

        var result= new CreditDues();
        result.setId("322323223");
        result.setIdCredit(monoDto.getIdCredit());
        result.setNroDues(monoDto.getNroDues());
        result.setAmount(monoDto.getAmount());

        when(creditService.payment(monoDto)).thenReturn(Mono.just(result));

        webTestClient.post().uri("/credit/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(monoDto),CreditDuesDto.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void findById() {
        var monoDto = new Credit("70150585sdsds","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate());

        when(creditService.findById("70150585sdsds")).thenReturn(Mono.just(monoDto));

        var responseFlux= webTestClient.get().uri("/credit/70150585sdsds")
            .exchange()
            .expectStatus().isOk()
            .returnResult(Credit.class)
            .getResponseBody();


        StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext( new Credit("70150585sdsds","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate()))
            .verifyComplete();
    }

    @Test
    void findAll() {

        var empFlux = Flux.just(
            new Credit("70150585sdsds","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate()),
            new Credit("sdfsdfasdfsss","CUSTOMER02","PRODUCT03", BigDecimal.valueOf(44511),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate())
        );

        when(creditService.findAll()).thenReturn(empFlux);

        var responseFlux= webTestClient.get().uri("/credit")
            .exchange()
            .expectStatus().isOk()
            .returnResult(Credit.class)
            .getResponseBody();


        StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext( new Credit("70150585sdsds","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate()))
            .expectNext( new Credit("sdfsdfasdfsss","CUSTOMER02","PRODUCT03", BigDecimal.valueOf(44511),20,10,1,BigDecimal.valueOf(100),"active",BigDecimal.ZERO, DateUtil.getStartCurrentDate()))
            .verifyComplete();

    }


}
