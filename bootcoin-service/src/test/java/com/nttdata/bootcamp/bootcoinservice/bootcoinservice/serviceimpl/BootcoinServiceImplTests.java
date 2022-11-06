package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.serviceimpl;


import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.BootcoinService;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.BootcoinServiceImpl;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.mappers.MapperBootcoin;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure.BootcoinRepository;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure.BootcointMovementsRepository;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.Bootcoin;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.BootcoinMovements;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.MovementsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
class BootcoinServiceImplTests {

  @TestConfiguration
  static class BootcoinServiceImplTestsConfiguration {
    @Bean
    public BootcoinService bootcoinService() {
      return new BootcoinServiceImpl();
    }

    @Bean
    public MapperBootcoin mapperBootcoin(){
      return new MapperBootcoin();
    }

    @MockBean
    public BootcoinRepository bootcoinRepository;

    @MockBean
    public BootcointMovementsRepository movementsRepository;
  }

  @Autowired
  private BootcoinService bootcoinService;

  @Autowired
  private BootcoinRepository bootcoinRepository;

  @Autowired
  private BootcointMovementsRepository movementsRepository;

  @Autowired
  private MapperBootcoin mapperBootcoin;


  @Test
	void create() {
    Bootcoin bootcoin = Bootcoin.builder()
      .numberDocument("70150525")
      .documentType("DNI")
      .phone("986434107")
      .names("Alexander Bejarano Aguilar")
      .balance(BigDecimal.ZERO)
      .email("ales.bejarano@gmail.com")
      .createAt(LocalDate.now())
      .build();

    when(bootcoinRepository.insert(bootcoin)).thenReturn(Mono.just(bootcoin));

    BootcoinDto request = BootcoinDto.builder()
      .numberDocument("70150525")
      .documentType("DNI")
      .phone("986434107")
      .names("Alexander Bejarano Aguilar")
      .balance(BigDecimal.ZERO)
      .email("ales.bejarano@gmail.com")
      .build();

    BootcoinDto response = BootcoinDto.builder()
      .numberDocument("70150525")
      .documentType("DNI")
      .phone("986434107")
      .names("Alexander Bejarano Aguilar")
      .balance(BigDecimal.ZERO)
      .email("ales.bejarano@gmail.com")
      .build();


    StepVerifier.create(bootcoinService.create(request))
      .expectNext(response)
      .expectComplete()
      .verify();

  }


  @Test
  void findByPhone() {

    var result= Bootcoin.builder()
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .build();


    when(bootcoinRepository.findByPhone("986434107")).thenReturn(Mono.just(result));



    StepVerifier.create(bootcoinService.findByPhone("986434107"))
      .expectNext(mapperBootcoin.toDto(result))
      .expectComplete()
      .verify();
  }


  @Test
  void addMovements() {

    var movement = BootcoinMovements.builder()
      .bootcoinId("12345678")
      .description("movement")
      .amount(BigDecimal.TEN)
      .date(LocalDate.now()).build();


    var initialBoot =  Bootcoin.builder()
      .id("12345678")
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .balance(BigDecimal.ZERO)
      .build();


    var finalBoot =  Bootcoin.builder()
      .id("12345678")
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .balance(BigDecimal.TEN)
      .build();


    when(bootcoinRepository.findByPhone("934819535")).thenReturn(Mono.just(initialBoot));
    when(movementsRepository.insert(movement)).thenReturn(Mono.just(movement));
    when(bootcoinRepository.save(finalBoot)).thenReturn(Mono.just(finalBoot));


    var parameter = MovementsDto.builder()
      .phone("934819535")
      .description("movement")
      .amount(BigDecimal.TEN)
      .date(LocalDate.now()).build();


    StepVerifier.create(bootcoinService.addMovements(parameter))
      .expectNext(mapperBootcoin.toDto(finalBoot))
      .expectComplete()
      .verify();

  }


  @Test
  void addMovementsError() {

    var movement = BootcoinMovements.builder()
      .bootcoinId("12345678")
      .description("movement")
      .amount(BigDecimal.TEN.negate())
      .date(LocalDate.now()).build();


    var initialBoot =  Bootcoin.builder()
      .id("12345678")
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .balance(BigDecimal.ZERO)
      .build();


    var finalBoot =  Bootcoin.builder()
      .id("12345678")
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .balance(BigDecimal.TEN)
      .build();


    when(bootcoinRepository.findByPhone("934819535")).thenReturn(Mono.just(initialBoot));
    when(movementsRepository.insert(movement)).thenReturn(Mono.just(movement));
    when(bootcoinRepository.save(finalBoot)).thenReturn(Mono.just(finalBoot));


    var parameter = MovementsDto.builder()
      .phone("934819535")
      .description("movement")
      .amount(BigDecimal.TEN.negate())
      .date(LocalDate.now()).build();


    StepVerifier.create(bootcoinService.addMovements(parameter))
      .expectError(InsufficientResourcesException.class)
      .verify();

  }

  @Test
  void findAll() {

    var bootcoinOne =  Bootcoin.builder()
      .id("12345678")
      .numberDocument("70150525")
      .documentType("DNI").phone("986434107")
      .names("ALEXNADER BEJARANO")
      .email("bejarano@gmail.com")
      .balance(BigDecimal.ZERO)
      .build();

    var bootcoinTwo =  Bootcoin.builder()
      .id("154485477")
      .numberDocument("70150525")
      .documentType("DNI").phone("3232323")
      .names("Angel alvarado")
      .email("angel@gmail.com")
      .balance(BigDecimal.TEN)
      .build();

    when(bootcoinRepository.findAll()).thenReturn(Flux.just(bootcoinOne, bootcoinTwo));

    StepVerifier.create(bootcoinService.findAll())
      .expectNext(mapperBootcoin.toDto(bootcoinOne))
      .expectNext(mapperBootcoin.toDto(bootcoinTwo))
      .expectComplete()
      .verify();



  }


}
