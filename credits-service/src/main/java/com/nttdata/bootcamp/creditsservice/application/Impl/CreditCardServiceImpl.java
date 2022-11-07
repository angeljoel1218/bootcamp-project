package com.nttdata.bootcamp.creditsservice.application.Impl;

import com.nttdata.bootcamp.creditsservice.application.CreditCardService;
import com.nttdata.bootcamp.creditsservice.application.utils.DateUtil;
import com.nttdata.bootcamp.creditsservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.creditsservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditCardMonthlyRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditCardRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.TransactionCreditCardRepository;
import com.nttdata.bootcamp.creditsservice.model.*;
import com.nttdata.bootcamp.creditsservice.model.constant.Category;
import com.nttdata.bootcamp.creditsservice.model.constant.CreditStatus;
import com.nttdata.bootcamp.creditsservice.model.constant.ProductType;
import com.nttdata.bootcamp.creditsservice.model.constant.TypeTransaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @since 2022
 */
@Slf4j
@Service
public class CreditCardServiceImpl implements CreditCardService {


    @Autowired
    private CustomerFeignClient customerFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private TransactionCreditCardRepository transactionCreditRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    CreditCardMonthlyRepository monthlyRepository;

    @Override
    public Mono<CreditCard> create(CreditCard creditCard) {
        return customerFeignClient.findById(creditCard.getIdCustomer()).flatMap(customer->{
            return productFeignClient.findById(creditCard.getIdProduct())
              .filter(p->p.getCategory() == Category.ACTIVE )
              .flatMap(product-> {

                if (customer.isItsPersonal()
                  && product.getProductTypeId() != ProductType.PERSONAL_CREDIT_CARD){
                  return   Mono.error(new InterruptedException(
                    "this product is not personal card"));
                }

                  if (customer.isItsCompany()
                    &&  product.getProductTypeId() != ProductType.BUSINESS_CREDIT_CARD){
                     return Mono.error(new InterruptedException(
                       "this product is not business card"));
                  }

                  creditCard.setAmountUsed(BigDecimal.ZERO);
                  creditCard.setStatus("active");
                  creditCard.setCreateDate(new Date());

                return  Mono.just(creditCard).flatMap(creditCardRepository::insert);
              })
              .switchIfEmpty(Mono.error(new InterruptedException(
                "product type active  not found")));
        }).switchIfEmpty(Mono.error(new InterruptedException(
          "Customer not found")));
    }

    @Override
    public Mono<CreditCard> update(Mono<CreditCard> creditCardMono, String id) {
        return creditCardRepository.findById(id)
                .flatMap(t -> creditCardMono)
                .doOnNext(e -> e.setId(id))
                .flatMap(creditCardRepository::save);
    }

    @Override
    public Mono<Void> delete(String id) {
        return creditCardRepository.deleteById(id);
    }

    @Override
    public Mono<CreditCard> findById(String id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public Flux<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    @Override
    public Mono<String> payment(TransactionCreditCard transactionCredit) {
        return creditCardRepository.findById(transactionCredit
          .getIdCredit()).flatMap(credit -> {
          BigDecimal totalUse = credit.getAmountUsed()
            .subtract(transactionCredit.getAmount());

          transactionCredit.setDate(new Date());
          transactionCredit.setType(TypeTransaction.PAYMENT);
          credit.setAmountUsed(totalUse);

          return creditCardRepository.save(credit)
              .flatMap(updateCredit->Mono.just(transactionCredit)
                .flatMap(transactionCreditRepository::insert)
                .flatMap( createTrans-> payMonthlyStatus(updateCredit, createTrans)))
              .then(Mono.just("payment completed successfully"));
        }).switchIfEmpty(Mono.error(new InterruptedException("credit card not found")));

    }


    @Override
    public Mono<String> charge(TransactionCreditCard transactionCredit) {
        return creditCardRepository.findById(transactionCredit.getIdCredit()).flatMap(credit -> {
          BigDecimal totalUse = credit.getAmountUsed().add(transactionCredit.getAmount());
          if (totalUse.compareTo(credit.getLimitAmount()) > 0){
            return Mono.error(new InterruptedException("You have exceeded the allowed limit"));
          };

          transactionCredit.setDate(new Date());
          transactionCredit.setType(TypeTransaction.CHARGE);

          credit.setAmountUsed(totalUse);

          return creditCardRepository.save(credit)
            .flatMap(updateCredit->Mono.just(transactionCredit)
                .flatMap(transactionCreditRepository::insert)
                .flatMap(createTrans->registerCreditCardMonthly(createTrans, updateCredit))
              ).then(Mono.just("charge completed successfully"));
          });
    }


    private Mono<CreditCardMonthly> payMonthlyStatus(CreditCard updateCredit,
                                                     TransactionCreditCard createTrans) {
        return monthlyRepository.findFirstByIdCreditCardAndStatusOrderByEndDateAsc(
          createTrans.getIdCredit(), CreditStatus.PENDING)
          .filter(mt-> createTrans.getAmount().compareTo(mt.getAmount()) > 0
            && createTrans.getDate().after(mt.getEndDate()))
          .flatMap(pendingMonthly->{
            BigDecimal rest = createTrans.getAmount()
              .subtract(pendingMonthly.getAmount());
            pendingMonthly.setPaidDate(new Date());
            pendingMonthly.setStatus(CreditStatus.PAYED);

            if (rest.compareTo(BigDecimal.ZERO) > 0){
              createTrans.setAmount(rest);
              payMonthlyStatus(updateCredit, createTrans);
            }
            return monthlyRepository.save(pendingMonthly);
          })
          .switchIfEmpty(registerCreditCardMonthly(createTrans, updateCredit));
    }

    private Mono<CreditCardMonthly>  registerCreditCardMonthly(TransactionCreditCard transaction,
                                                               CreditCard creditCard){
      return monthlyRepository.findFirstByIdCreditCardOrderByEndDateDesc(transaction.getIdCredit())
        .filter(tf->transaction.getDate().before(tf.getEndDate())
          || transaction.getDate().equals(tf.getEndDate()))
          .switchIfEmpty(insertNewMonthly(transaction, creditCard))
          .flatMap(t->{
                BigDecimal newAmount =  t.getAmount()
                  .add(transaction.getAmount()
                    .multiply(affectationOperationMonthly(transaction.getType())));
                t.setAmount(newAmount);
                return Mono.just(t).flatMap(monthlyRepository::save);
          });

    }

    private Mono<CreditCardMonthly> insertNewMonthly(TransactionCreditCard transaction,
                                                     CreditCard creditCard) {
       return monthlyRepository.findFirstByIdCreditCardOrderByEndDateDesc(transaction.getIdCredit())
         .map(last-> DateUtils.addDays(last.getEndDate(), 1) )
         .switchIfEmpty(Mono.just(new Date()))
         .flatMap(dateStart->{
              dateStart  = DateUtil.setStartDate(dateStart);
              Date dateOfEnd = DateUtil.getDateEndByDayOfMonth(creditCard.getClosingDay());
              Date dateOfPay = DateUtil.getDateEndByDayOfMonth(creditCard.getDayOfPay());

              dateOfEnd = dateOfEnd.before(dateStart) ?
                DateUtils.addMonths(dateOfEnd, 1) : dateOfEnd;

              dateOfPay = dateOfPay.before(dateOfEnd) ?
                DateUtils.addMonths(dateOfPay, 1) : dateOfPay;


              CreditCardMonthly newMonthly = new CreditCardMonthly();
              newMonthly.setIdCreditCard(creditCard.getId());
              newMonthly.setStartDate(dateStart);
              newMonthly.setEndDate(dateOfEnd);
              newMonthly.setExpireDate(dateOfPay);
              newMonthly.setStatus(CreditStatus.PENDING);
              newMonthly.setAmount(BigDecimal.ZERO);
              return Mono.just(newMonthly);
          }).flatMap(monthlyRepository::insert);
    }

    private BigDecimal affectationOperationMonthly(TypeTransaction typeTransaction){
        return Objects.equals(typeTransaction, TypeTransaction.CHARGE) ?
          BigDecimal.ONE : BigDecimal.ONE.negate();
    }

    @Override
    public Flux<CreditCard> findByIdCustomer(String id) {

      return creditCardRepository.findByIdCustomer(id);
    }

    @Override
    public Flux<TransactionCreditCard> findTransactionByIdCredit(String id) {
        return transactionCreditRepository.findByIdCredit(id);
    }

    @Override
    public Mono<List<TransactionCreditCard>> findLastTransactionByIdCredit(String idCredit,
                                                                           Integer limit) {
        return transactionCreditRepository
          .findByIdCreditOrderByDateDesc(idCredit)
          .collectList().map(t->{
            return t.stream().limit(limit).collect(Collectors.toList());
        });
    }

    @Override
    public Mono<Boolean> findIsCustomerHaveDebs(String idCustomer) {
      return  creditCardRepository.findByIdCustomer(idCustomer)
          .flatMap(listCredits-> monthlyRepository
            .findFirstByIdCreditCardAndStatusOrderByEndDateAsc(listCredits.getId(),
              CreditStatus.PENDING)).collectList().map(list->{
            return   list.stream().anyMatch(t -> {
               return t.getExpireDate().before(DateUtil.getStartCurrentDate());
              });
          }).switchIfEmpty(Mono.just(Boolean.FALSE));
    }

    @Override
    public Flux<CreditCard> findByCreateDateBetweenAndIdProduct(Date start,
                                                                Date end,
                                                                String idProduct) {

        return creditCardRepository.findByCreateDateBetweenAndIdProduct(start,
            end,
            idProduct);
    }

  @Override
  public Mono<List<TransactionCreditCard>> findLastTenTransactionByIdCredit(String idCredit) {
    return transactionCreditRepository.findByIdCreditOrderByDateDesc(idCredit)
      .collectList().map(t -> {
      return t.stream().limit(10).collect(Collectors.toList());
    });
  }
}
