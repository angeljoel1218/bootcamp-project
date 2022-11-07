package com.nttdata.bootcamp.reportservice.feignclients.fallback;

import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.model.dto.AccountDto;
import com.nttdata.bootcamp.reportservice.model.dto.CreditCardDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @since 2022
 */
@Log4j2
public class AccountFeignClientFallBack implements AccountFeignClient {

  @Override
  public Flux<AccountDto> findAccountByHolderId(String id) {
    log.info("findAccountByHolderId service no available  {}", id);
    return  Flux.just();
  }

  @Override
  public Flux<TransactionDto> findTransactionByAccountId(String id) {
    log.info("findTransactionByAccountId service no available  {}", id);
    return   Flux.just();
  }

  @Override
  public Flux<CreditCardDto> findByCreateDateBetweenAndProductId(Date startDate,
                                                                 Date endDate,
                                                                 String idProduct) {
    log.info("findTransactionByAccountId service no available");
    return   Flux.just();
  }
}
