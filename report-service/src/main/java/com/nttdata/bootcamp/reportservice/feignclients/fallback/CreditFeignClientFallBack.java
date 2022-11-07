package com.nttdata.bootcamp.reportservice.feignclients.fallback;

import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CreditFeignClient;
import com.nttdata.bootcamp.reportservice.model.dto.*;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import java.util.Date;


/**
 *
 * @since 2022
 */
@Log4j2
public class CreditFeignClientFallBack implements CreditFeignClient {

    @Override
    public Flux<CreditDto> findCreditByIdCustomer(String idCustomer) {
        log.info("findCreditByIdCustomer  service no available  {}", idCustomer);
        return Flux.just();
    }

    @Override
    public Flux<CreditDuesDto> findCreditDuesByIdCredit(String idCredit) {
        log.info("findCreditDuesByIdCredit  service no available  {}", idCredit);
        return Flux.just();
    }

    @Override
    public Flux<CreditDto> findCreditByCreateDateBetweenAndIdProduct(Date startDate,
                                                                     Date endDate,
                                                                     String idProduct) {

        log.info("findCreditByCreateDateBetweenAndIdProduct " +
          " service no available  startDate={},endDate={}, idProduct={}", startDate,
          endDate, idProduct);

        return Flux.just();
    }

    @Override
    public Flux<CreditCardDto> findCreditCardByIdCustomer(String idCustomer) {
        log.info("findCreditCardByIdCustomer  service no available  {}", idCustomer);
        return Flux.just();
    }

    @Override
    public Flux<TransactionCreditCardDto> findTransactionByIdCreditCard(String idCredit) {
        log.info("findTransactionByIdCreditCard  service no available  {}", idCredit);
        return Flux.just();
    }
    @Override public Flux<CreditCardDto> findByCreditCardCreateDateBetweenAndIdProduct(
      Date startDate,
      Date endDate,
      String idProduct) {

      log.info("findByCreditCardCreateDateBetweenAndIdProduct " +
        " service no available  startDate={},endDate={}, idProduct={}",
        startDate, endDate, idProduct);
      return Flux.just();
    }
}
