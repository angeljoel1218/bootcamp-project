package com.nttdata.bootcamp.reportservice.feignclients;


import com.nttdata.bootcamp.reportservice.model.dto.AccountDto;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDto;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDuesDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(value =  "${feign.service.credit.name}")
public interface CreditFeignClient {

  @GetMapping("credit/customer/{id}")
  public Flux<CreditDto> findByIdCustomer (@PathVariable("id") String idCustomer);

  @GetMapping("credit/dues/{idCredit}")
  public Flux<CreditDuesDto> findCreditDuesByIdCredit (@PathVariable("idCredit") String idCredit);

}
