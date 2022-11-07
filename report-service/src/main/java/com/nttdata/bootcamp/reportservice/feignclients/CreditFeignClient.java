package com.nttdata.bootcamp.reportservice.feignclients;


import com.nttdata.bootcamp.reportservice.feignclients.fallback.CreditFeignClientFallBack;
import com.nttdata.bootcamp.reportservice.model.dto.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

import java.util.Date;

/**
 *
 * @since 2022
 */
@ReactiveFeignClient(value =  "${feign.service.credit.name}",
  fallback = CreditFeignClientFallBack.class)
public interface CreditFeignClient {
  @GetMapping("credit/customer/{id}")
  public Flux<CreditDto> findCreditByIdCustomer (@PathVariable("id") String idCustomer);

  @GetMapping("credit/dues/{idCredit}")
  public Flux<CreditDuesDto> findCreditDuesByIdCredit (@PathVariable("idCredit") String idCredit);


  @GetMapping("credit/product")
  public Flux<CreditDto> findCreditByCreateDateBetweenAndIdProduct (
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate,
    @RequestParam String idProduct);


  @GetMapping("credit/card/customer/{id}")
  public Flux<CreditCardDto> findCreditCardByIdCustomer (@PathVariable("id") String idCustomer);

  @GetMapping("credit/card/transaction/{idCredit}")
  public Flux<TransactionCreditCardDto> findTransactionByIdCreditCard (
    @PathVariable("idCredit") String idCredit);

  @GetMapping("credit/card/product")
  public Flux<CreditCardDto> findByCreditCardCreateDateBetweenAndIdProduct (
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate,
    @RequestParam String idProduct);

}
