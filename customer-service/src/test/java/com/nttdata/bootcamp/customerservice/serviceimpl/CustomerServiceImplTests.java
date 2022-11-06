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
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("should validate the customer creation")
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
  @DisplayName("should validate the customer update, error")
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



  @DisplayName("should validate the customer delete, error")
  @Test
  void delete() {
    var id = "70150585sdsds";
    when(customerRepository.findById(id)).thenReturn(Mono.just(new Customer()));

    StepVerifier.create(customerService.delete(id))
      .expectError(RuntimeException.class)
      .verify();
  }

  @Test
  void findById() {
    var id="70150585sdsds";
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


    when(customerRepository.findById("70150585sdsds")).thenReturn(Mono.just(customer));



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

    StepVerifier.create(customerService.findById(id))
      .expectNext(response)
      .expectComplete()
      .verify();
  }

  @Test
  void findAll() {

    Customer customerOne = Customer.builder()
      .typeCustomer(TypeCustomer.PERSONAL)
      .name("Alexander")
      .lastName("Bejarano")
      .businessName("Company")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("70150525")
      .status(Constants.STATUS_CREATED)
      .typeProfile(TypeProfile.VIP)
      .build();

    Customer customerTwo = Customer.builder()
      .typeCustomer(TypeCustomer.COMPANY)
      .name("Alejandro")
      .lastName("Marioano")
      .businessName("sdfsd")
      .emailAddress("ales.bejarano@gmail.com")
      .numberDocument("232323")
      .status(Constants.STATUS_CREATED)
      .typeProfile(TypeProfile.PYME)
      .build();

    when(customerRepository.findAll()).thenReturn(Flux.just(customerOne,customerTwo));


    StepVerifier.create(customerService.findAll())
      .expectSubscription()
      .expectNext(

        CustomerDto.builder()
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
          .build()
      )
      .expectNext(
        CustomerDto.builder()
          .typeCustomer(TypeCustomer.COMPANY)
          .name("Alejandro")
          .lastName("Marioano")
          .businessName("sdfsd")
          .emailAddress("ales.bejarano@gmail.com")
          .numberDocument("232323")
          .typeProfile(TypeProfile.PYME)
          .status(Constants.STATUS_CREATED)
          .itsVip(false)
          .itsPyme(true)
          .itsCompany(true)
          .itsPersonal(false)
          .build()
      ).verifyComplete();


  }

}
