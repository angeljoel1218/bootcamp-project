package com.nttdata.bootcamp.creditsservice.application.Impl;

import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.application.exceptions.CreditException;
import com.nttdata.bootcamp.creditsservice.application.mappers.MapperCredit;
import com.nttdata.bootcamp.creditsservice.application.utils.DateUtil;
import com.nttdata.bootcamp.creditsservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.creditsservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditDuesRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditRepository;
import com.nttdata.bootcamp.creditsservice.model.*;
import com.nttdata.bootcamp.creditsservice.model.constant.Category;
import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import com.nttdata.bootcamp.creditsservice.model.constant.ProductType;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * javadoc.
 * Bank
 * @since 2022
 */

@Slf4j
@Service
public class CreditServiceImpl implements CreditService {


  @Autowired
  private CreditRepository creditRepository;

  @Autowired
  private CreditDuesRepository creditDuesRepository;

  @Autowired
  private CustomerFeignClient customerFeignClient;

  @Autowired
  private ProductFeignClient productFeignClient;

  @Autowired
  private MapperCredit mapperCredit;


  @Override
  public Mono<Credit> create(Credit credit) {
    return customerFeignClient.findById(credit.getIdCustomer()).flatMap(customer -> {
      return productFeignClient.findById(credit.getIdProduct())
        .filter(p -> p.getCategory() == Category.ACTIVE).flatMap(product -> {
          return creditRepository.findByIdCustomer(credit.getIdCustomer()).count()
            .flatMap(countAccounts -> {
              if (countAccounts > 0 && customer.isItsPersonal()) {
                return Mono.error(new CreditException("cannot have more than one credit"));
              }

              if (customer.isItsPersonal()
                  &&  product.getProductTypeId() != ProductType.PERSONAL_CREDIT) {
                return   Mono.error(
                          new InterruptedException("this product is not personal credit"));
              }
              if (customer.isItsCompany()
                  && product.getProductTypeId() != ProductType.BUSINESS_CREDIT) {
                return Mono.error(
                        new InterruptedException("this product is not business credit"));
              }

              credit.setStatus("active");
              credit.setCreateDate(new Date());
              return  Mono.just(credit).flatMap(creditRepository::insert)
                .flatMap(this::createCreditDuesByCredit);
            });
        }).switchIfEmpty(Mono.error(new CreditException("Product type active not found")));
    }).switchIfEmpty(Mono.error(new CreditException("Customer not found")));

  }

  private Mono<Credit>  createCreditDuesByCredit(Credit credit) {
    log.info("Create dues");

    Date dayOfPay = DateUtil.getDateEndByDayOfMonth(credit.getDayOfPay());
    Date currentDate = DateUtil.getStartCurrentDate();
    Integer intervalDues = credit.getIntervalDues();

    Date expirationDate = dayOfPay.before(currentDate)
                          ? DateUtils.addMonths(dayOfPay, intervalDues) : dayOfPay;

    BigDecimal totalAmount = credit.getAmountCredit();
    BigDecimal totalInterest = credit.getInterestAmount();
    Integer numberTotalOfDues = credit.getDues();
    Integer numberDues = 1;
    while (numberDues <= numberTotalOfDues) {

      BigDecimal duesAmount = totalAmount.divide(BigDecimal.valueOf(numberTotalOfDues),
                                                              4, RoundingMode.HALF_UP);

      BigDecimal interestAmount = totalInterest.divide(BigDecimal.valueOf(numberTotalOfDues),
                                                              4, RoundingMode.HALF_UP);

      CreditDues nreDue = new CreditDues();
      nreDue.setIdCredit(credit.getId());
      nreDue.setNroDues(numberDues);
      nreDue.setAmount(duesAmount);
      nreDue.setInterest(interestAmount);
      nreDue.setExpirationDate(expirationDate);
      nreDue.setStatus(CreditStatus.PENDING);
      nreDue.setTotalAmount(duesAmount.add(interestAmount));


      creditDuesRepository.insert(nreDue).subscribe();
      expirationDate = DateUtils.addMonths(expirationDate, intervalDues);
      numberDues++;
    }
    return Mono.just(credit);
  }

  @Override
  public Mono<Credit> update(Mono<Credit> creditMono, String id) {
    return creditRepository.findById(id)
        .flatMap(t -> creditMono)
        .doOnNext(e -> e.setId(id))
        .flatMap(creditRepository::save).flatMap(t -> {
          return creditDuesRepository.deleteByIdCredit(t.getId()).flatMap(v -> {
            return  Mono.just(t).flatMap(this::createCreditDuesByCredit);
          });
        });
  }

  @Override
  public Mono<Void> delete(String id) {
    return creditRepository.findById(id).then(creditDuesRepository.deleteById(id));
  }

  @Override
  public Mono<Credit> findById(String id) {
    return creditRepository.findById(id);
  }

  @Override
  public Flux<Credit> findAll() {
    return creditRepository.findAll();
  }

  @Override
  public Mono<CreditDues> payment(CreditDuesDto creditDuesDto) {
    return creditDuesRepository.findByIdCreditAndNroDues(creditDuesDto.getIdCredit(),
        creditDuesDto.getNroDues())
      .flatMap(t -> {
        if (creditDuesDto.getAmount().compareTo(t.getTotalAmount()) != 0) {
          log.info("The amount of the due must be");
          return Mono.error(
                            new CreditException(String.format("The amount of the due must be %s",
                            t.getTotalAmount().toString())));
        }

        if (t.getStatus().equals(CreditStatus.PAYED)) {
          return Mono.error(
                            new CreditException(String.format("The due amount have done paid at %s",
                            t.getPaidDate())));
        }

        t.setPaidDate(new Date());
        t.setStatus(CreditStatus.PAYED);
        return Mono.just(t);
      }).flatMap(creditDuesRepository::save).doOnNext(due -> {
        creditRepository.findById(creditDuesDto.getIdCredit())
         .map(t -> {
           t.setAmountPayed(t.getAmountPayed().add(due.getAmount()));
           return t;
         }).flatMap(creditRepository::save);
      }).switchIfEmpty(Mono.error(new CreditException("Due have not exist")));
  }


  @Override
  public Flux<Credit> findByIdCustomer(String id) {
    return creditRepository.findByIdCustomer(id);
  }

  @Override
  public Mono<Boolean> findIsCustomerHaveDebs(String idCustomer) {
    return creditRepository.findByIdCustomer(idCustomer)
      .flatMap(cre -> creditDuesRepository
        .findFirstByIdCreditAndStatusOrderByExpirationDateAsc(cre.getId(),
        CreditStatus.PENDING)).collectList()
       .map(list -> {
         return   list.stream().anyMatch(t -> {
           return t.getExpirationDate().before(DateUtil.getStartCurrentDate());
         });
       }).switchIfEmpty(Mono.just(Boolean.FALSE));
  }

  @Override
  public Flux<Credit> findByCreateDateBetweenAndIdProduct(Date start, Date end,  String idProduct) {
    return creditRepository.findByCreateDateBetweenAndIdProduct(start, end, idProduct);
  }

  @Override
  public Flux<CreditDues> findCreditDuesByIdCredit(String idCredit) {
    return creditDuesRepository.findByIdCredit(idCredit);
  }


}
