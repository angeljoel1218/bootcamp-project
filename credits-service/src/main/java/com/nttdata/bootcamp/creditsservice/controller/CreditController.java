package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * javadoc.
 * Bank
 * @since 2022
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("credit")
public class CreditController {
  @Autowired
  private CreditService creditService;

  @GetMapping()
  public Flux<Credit> findAll() {
    return   creditService.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Credit> findById(@PathVariable("id") String id) {
    return creditService.findById(id);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Credit> create(@RequestBody @Valid Credit creditMono) {
    Map<String, Object> response = new HashMap<>();
    return creditService.create(creditMono);
  }

  @PostMapping("/payment")
  public Mono<ResponseEntity<CreditDues>> payment(@RequestBody CreditDuesDto paymentCredit) {
    return creditService.payment(paymentCredit)
      .map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
        log.info("Error:" + e.getMessage());
        return Mono.just(ResponseEntity.badRequest().build());
      }).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/customer/{id}")
  public Flux<Credit> findByIdCustomer(@PathVariable("id") String id) {
    return  creditService.findByIdCustomer(id);
  }

  @GetMapping("/dues/{idCredit}")
  public Flux<CreditDues> findCreditDuesByIdCredit(@PathVariable("idCredit") String id) {
    return creditService.findCreditDuesByIdCredit(id);
  }

  @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Credit> findByCreateDateBetweenAndIdProduct(
                              @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
                              @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate,
                              @RequestParam String idProduct) {

    return creditService.findByCreateDateBetweenAndIdProduct(startDate, endDate, idProduct);

  }
}
