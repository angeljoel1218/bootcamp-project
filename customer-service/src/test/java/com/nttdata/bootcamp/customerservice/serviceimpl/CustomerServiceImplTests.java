package com.nttdata.bootcamp.customerservice.serviceimpl;

import com.nttdata.bootcamp.customerservice.aplication.CustomerService;
import com.nttdata.bootcamp.customerservice.aplication.CustomerServiceImpl;
import com.nttdata.bootcamp.customerservice.aplication.mappers.MapperCustomer;
import com.nttdata.bootcamp.customerservice.controller.CustomerController;
import com.nttdata.bootcamp.customerservice.infraestructure.CustomerRepository;
import com.nttdata.bootcamp.customerservice.model.Customer;
import com.nttdata.bootcamp.customerservice.model.constants.Constants;
import com.nttdata.bootcamp.customerservice.model.constants.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.constants.TypeProfile;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTests {


  @TestConfiguration
  static class CurrentAccountServiceImplTestConfiguration {
    @Bean
    public CustomerService CustomerService() {
      return new CustomerServiceImpl();
    }

    @Bean
    public MapperCustomer mapperCustomer(){
      return new MapperCustomer();
    }

    @MockBean
    public CustomerRepository customerRepository;
  }

  @Autowired
  private CustomerRepository customerRepository;


  @Autowired
  private CustomerService customerService;


  @Test
	void create() {
    Customer customer = Customer.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .status(Constants.STATUS_CREATED)
      .typeProfile(TypeProfile.VIP)
      .build();

    when(customerRepository.insert(customer)).thenReturn(Mono.just(customer));


    CustomerDto request = CustomerDto.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .typeProfile(TypeProfile.VIP)
      .build();


    CustomerDto response = CustomerDto.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .typeProfile(TypeProfile.VIP)
      .status(Constants.STATUS_CREATED)
      .itsVip(true)
      .itsPyme(false)
      .itsCompany(false)
      .itsPersonal(true)
      .build();




    StepVerifier.create(customerService.create(request))
      .expectNext(response)
      .expectComplete()
      .verify();

  }


  @Test
  void update() {
    var id = "01505254514";

    CustomerDto request = CustomerDto.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .typeProfile(TypeProfile.VIP)
      .build();

    when(customerRepository.findById(id)).thenReturn(Mono.just(new Customer()));

    StepVerifier.create(customerService.update(request,id))
      .expectError(RuntimeException.class)
      .verify();
  }

  /*
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
*/
}
