package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditCardService;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/credit/card")
public class CreditCardController {

  @Autowired
  private CreditCardService creditCardService;

  @GetMapping()
  public Flux<CreditCard> findAll() {
    return creditCardService.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<CreditCard>> findById(@PathVariable("id") String id) {
    return creditCardService.findById(id).map(a -> ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public  Mono<CreditCard> create(@RequestBody @Valid CreditCard creditCard) {
    return  creditCardService.create(creditCard);
  }

  @PutMapping("/{id}")
  public  Mono<CreditCard> update(@RequestBody Mono<CreditCard> creditMono,
                                  @PathVariable String id) {
    return  creditCardService.update(creditMono, id);
  }

  @DeleteMapping("/{id}")
  public  Mono<Void> delete(@PathVariable String id) {
    return creditCardService.delete(id);
  }


  @PostMapping("/payment")
  public  Mono<ResponseEntity<String>> payment(
      @RequestBody TransactionCreditCard transactionCreditCard) {

    return creditCardService.payment(transactionCreditCard)
      .map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
        log.info("Error:" + e.getMessage());
        return Mono.just(ResponseEntity.badRequest().build());
      }).defaultIfEmpty(ResponseEntity.notFound().build());

  }

  @PostMapping("/charge")
  public  Mono<ResponseEntity<String>> charge(
      @RequestBody TransactionCreditCard transactionCreditCard) {

    return creditCardService.charge(transactionCreditCard)
      .map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
        log.info("Error:" + e.getMessage());
        return Mono.just(ResponseEntity.badRequest().build());
      }).defaultIfEmpty(ResponseEntity.notFound().build());

  }

  @GetMapping("/customer/{id}")
  public Flux<CreditCard>  findByIdCustomer(@PathVariable("id") String id) {
    return  creditCardService.findByIdCustomer(id);
  }

  @GetMapping("/transaction/{idCredit}")
  public Flux<TransactionCreditCard>  findTransactionByIdCredit(
      @PathVariable("idCredit") String idCredit) {

    return  creditCardService.findTransactionByIdCredit(idCredit);
  }

  @GetMapping("/customer-debts/{idCustomer}")
  public Mono<Boolean>  findCustomerDebs(@PathVariable("idCustomer") String idCustomer) {
    return  creditCardService.findIsCustomerHaveDebs(idCustomer);
  }

  @GetMapping("/transaction-lasted/{idCredit}")
  public Mono<List<TransactionCreditCard>> findLastTenTransactionByIdCredit(
      @PathVariable("idCredit") String idCredit) {
    return  creditCardService.findLastTenTransactionByIdCredit(idCredit);
  }

  @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<CreditCard> findByCreateDateBetweenAndIdProduct(

                                @RequestParam
                                @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,

                                @RequestParam
                                @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate,

                                @RequestParam String idProduct) {

    return creditCardService.findByCreateDateBetweenAndIdProduct(startDate, endDate, idProduct);
  }

}
