package com.nttdata.bootcamp.creditsservice;

import com.nttdata.bootcamp.creditsservice.application.CreditCardService;
import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.application.utils.DateUtil;
import com.nttdata.bootcamp.creditsservice.controller.CreditCardController;
import com.nttdata.bootcamp.creditsservice.controller.CreditController;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import com.nttdata.bootcamp.creditsservice.model.constant.TypeTransaction;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CreditCardController.class)
class CreditsCardServiceApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreditCardService creditService;


    @Test
	void create() {

        var monoDto = new CreditCard(null,"CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate());

        when(creditService.create(monoDto)).thenReturn(Mono.just(monoDto));

        webTestClient.post().uri("/credit/card")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(monoDto),Credit.class)
            .exchange()
            .expectStatus().isCreated();
	}


    @Test
    void charge() {
        var monoDto = new TransactionCreditCard();
        monoDto.setIdCredit("1212121212");
        monoDto.setDescription("Cargo");
        monoDto.setAmount(BigDecimal.TEN);
        monoDto.setType(TypeTransaction.CHARGE);
        monoDto.setDate(DateUtil.getStartCurrentDate());


        when(creditService.charge(monoDto)).thenReturn(Mono.just("charge completed successfully"));

        webTestClient.post().uri("/credit/card/charge")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(monoDto),TransactionCreditCard.class)
            .exchange()
            .expectStatus().isOk();
    }


    @Test
    void findById() {
        var monoDto = new CreditCard("dfasdfasdfasdfasdfsad","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate());

        when(creditService.findById("dfasdfasdfasdfasdfsad")).thenReturn(Mono.just(monoDto));

        var responseFlux= webTestClient.get().uri("/credit/card/dfasdfasdfasdfasdfsad")
            .exchange()
            .expectStatus().isOk()
            .returnResult(CreditCard.class)
            .getResponseBody();

        StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext( new CreditCard("dfasdfasdfasdfasdfsad","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate()))
            .verifyComplete();
    }

    @Test
    void findAll() {

        var empFlux = Flux.just(
            new CreditCard("dfasdfasdfasdfasdfsad","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate()),
            new CreditCard("dfasdfasdfasdfasdfsad","CUSTOMER03","PRODUCT03", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate())
        );

        when(creditService.findAll()).thenReturn(empFlux);

        var responseFlux= webTestClient.get().uri("/credit/card")
            .exchange()
            .expectStatus().isOk()
            .returnResult(CreditCard.class)
            .getResponseBody();


        StepVerifier.create(responseFlux)
            .expectSubscription()
            .expectNext(  new CreditCard("dfasdfasdfasdfasdfsad","CUSTOMER01","PRODUCT01", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate()))
            .expectNext( new CreditCard("dfasdfasdfasdfasdfsad","CUSTOMER03","PRODUCT03", BigDecimal.valueOf(1000),BigDecimal.ZERO,20,10,"active",DateUtil.getStartCurrentDate()))
            .verifyComplete();

    }


}
