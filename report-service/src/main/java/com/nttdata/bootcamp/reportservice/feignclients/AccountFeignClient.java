package com.nttdata.bootcamp.reportservice.feignclients;

import com.nttdata.bootcamp.reportservice.feignclients.fallback.AccountFeignClientFallBack;
import com.nttdata.bootcamp.reportservice.model.dto.AccountDto;
import com.nttdata.bootcamp.reportservice.model.dto.CreditCardDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
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
@ReactiveFeignClient(value =  "${feign.service.accounts.name}",
  fallback = AccountFeignClientFallBack.class)
public interface AccountFeignClient {

  @GetMapping("account/customer/{holderId}")
  public  Flux<AccountDto>  findAccountByHolderId(@PathVariable("holderId") String id);

  @GetMapping("account/transaction/{accountId}/list")
  public Flux<TransactionDto> findTransactionByAccountId(@PathVariable("accountId") String id);

  @GetMapping("account/product")
  public Flux<CreditCardDto> findByCreateDateBetweenAndProductId (
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate,
    @RequestParam String idProduct);



}
