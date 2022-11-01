package com.nttdata.bootcamp.customerservice;

import com.nttdata.bootcamp.customerservice.aplication.CustomerService;
import com.nttdata.bootcamp.customerservice.controller.CustomerController;
import com.nttdata.bootcamp.customerservice.model.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.TypeProfile;
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
class CustomerServiceApplicationTests {


  @Autowired
  private WebTestClient webTestClient;


  @MockBean
  private CustomerService customerService;


  @Test
	void create() {
    var monoDto = new CustomerDto(TypeCustomer.PERSONAL,"Alexander","Bejarano","Company","ales.bejarano@gmail.com","70150525", TypeProfile.VIP);
    when(customerService.create(monoDto)).thenReturn(Mono.just(monoDto));

    webTestClient.post().uri("/customer/create")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .body(Mono.just(monoDto),CustomerDto.class)
      .exchange()
      .expectStatus().isCreated();


  }

  @Test
  void update() {
    var id = "70150585sdsds";
    var monoDto = new CustomerDto("70150585sdsds",TypeCustomer.PERSONAL,"Alexander","Bejarano","Company","ales.bejarano@gmail.com","70150525", TypeProfile.VIP);

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
    var monoDto = new CustomerDto("70150585sdsds",TypeCustomer.PERSONAL,"Alexander","Bejarano","Company","ales.bejarano@gmail.com","70150525", TypeProfile.VIP);

    when(customerService.findById("70150585sdsds")).thenReturn(Mono.just(monoDto));
    var responseFlux= webTestClient.get().uri("/customer/get/70150585sdsds")
      .exchange()
      .expectStatus().isOk()
      .returnResult(CustomerDto.class)
      .getResponseBody();

    StepVerifier.create(responseFlux)
      .expectSubscription()
      .expectNext(new CustomerDto("70150585sdsds",TypeCustomer.PERSONAL,"Alexander","Bejarano","Company","ales.bejarano@gmail.com","70150525", TypeProfile.VIP))
      .verifyComplete();
  }

  @Test
  void findAll() {

    var empFlux = Flux.just(
      new CustomerDto(TypeCustomer.PERSONAL,"Alexander","Bejarano","Company","ales.bejarano@gmail.com","70150525", TypeProfile.VIP),
      new CustomerDto(TypeCustomer.PERSONAL,"Angel","Alvarado","Other","angel@gmail.com","784574", null)
      );

    when(customerService.findAll()).thenReturn(empFlux);

    var responseFlux = webTestClient.get().uri("/customer/all")
      .exchange()
      .expectStatus().isOk()
      .returnResult(CustomerDto.class)
      .getResponseBody();

    StepVerifier.create(responseFlux)

      .expectSubscription()
      .expectNext(new CustomerDto(TypeCustomer.PERSONAL,"Alexander","Bejarano","Company","ales.bejarano@gmail.com","70150525", TypeProfile.VIP))
      .expectNext(new CustomerDto(TypeCustomer.PERSONAL,"Angel","Alvarado","Other","angel@gmail.com","784574", null))
      .verifyComplete();


  }
}
