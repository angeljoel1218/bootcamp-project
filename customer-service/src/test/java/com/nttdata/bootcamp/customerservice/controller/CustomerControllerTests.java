package com.nttdata.bootcamp.customerservice.controller;

import com.nttdata.bootcamp.customerservice.aplication.CustomerService;
import com.nttdata.bootcamp.customerservice.model.constants.Constants;
import com.nttdata.bootcamp.customerservice.model.constants.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.constants.TypeProfile;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
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
@WebFluxTest(CustomerController.class)
class CustomerControllerTests {


  @Autowired
  private WebTestClient webTestClient;


  @MockBean
  private CustomerService customerService;


  @Test
	void create() {


    var monoDto = CustomerDto.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .status(Constants.STATUS_CREATED)
      .typeProfile(TypeProfile.VIP)
      .build();

    when(customerService.create(monoDto)).thenReturn(Mono.just(monoDto));

    webTestClient.post().uri("/customer/create")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .body(Mono.just(monoDto),CustomerDto.class)
      .exchange()
      .expectStatus().isCreated();


  }

  @Test
  void createInvalidData() {

    webTestClient.post().uri("/customer/create")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .body(Mono.just(new CustomerDto()),CustomerDto.class)
      .exchange()
      .expectStatus().is5xxServerError();

  }

  @Test
  void update() {
    var id = "70150585sdsds";

    var monoDto = CustomerDto.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .status(Constants.STATUS_CREATED)
      .typeProfile(TypeProfile.VIP)
      .build();

    when(customerService.update(monoDto,id)).thenReturn(Mono.just(monoDto));

    webTestClient.put().uri("/customer/"+id)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .body(Mono.just(monoDto),CustomerDto.class)
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  void delete() {
    var id = "70150585sdsds";
    when(customerService.delete(id)).thenReturn(Mono.empty().then());
    webTestClient.delete().uri("/customer/delete/"+id)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  void findById() {

    var monoDto = CustomerDto.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .status(Constants.STATUS_CREATED)
      .typeProfile(TypeProfile.VIP)
      .build();


    when(customerService.findById("70150585sdsds")).thenReturn(Mono.just(monoDto));

    var responseFlux= webTestClient.get().uri("/customer/get/70150585sdsds")
      .exchange()
      .expectStatus().isOk()
      .returnResult(CustomerDto.class)
      .getResponseBody();

    StepVerifier.create(responseFlux)
      .expectSubscription()
      .expectNext(monoDto)
      .verifyComplete();
  }

  @Test
  void findAll() {

    var empFlux = Flux.just(
      CustomerDto.builder()
        .typeCustomer(TypeCustomer.PERSONAL)
        .name("Alexander")
        .lastName("Bejarano")
        .businessName("Company")
        .emailAddress("ales.bejarano@gmail.com")
        .numberDocument("70150525")
        .status(Constants.STATUS_CREATED)
        .typeProfile(TypeProfile.VIP)
        .build(),

      CustomerDto.builder()
        .typeCustomer(TypeCustomer.COMPANY)
        .businessName("Company")
        .emailAddress("ales.bejarano@gmail.com")
        .numberDocument("70150525")
        .status(Constants.STATUS_CREATED)
        .typeProfile(TypeProfile.PYME)
        .build()
      );

    when(customerService.findAll()).thenReturn(empFlux);

    var responseFlux = webTestClient.get().uri("/customer/all")
      .exchange()
      .expectStatus().isOk()
      .returnResult(CustomerDto.class)
      .getResponseBody();

    StepVerifier.create(responseFlux)

      .expectSubscription()
      .expectNext(
        CustomerDto.builder()
        .typeCustomer(TypeCustomer.PERSONAL)
        .name("Alexander")
        .lastName("Bejarano")
        .businessName("Company")
        .emailAddress("ales.bejarano@gmail.com")
        .numberDocument("70150525")
        .status(Constants.STATUS_CREATED)
        .typeProfile(TypeProfile.VIP)
        .build())
      .expectNext(
        CustomerDto.builder()
          .typeCustomer(TypeCustomer.COMPANY)
          .businessName("Company")
          .emailAddress("ales.bejarano@gmail.com")
          .numberDocument("70150525")
          .status(Constants.STATUS_CREATED)
          .typeProfile(TypeProfile.PYME)
          .build()
      )
      .verifyComplete();


  }
}
