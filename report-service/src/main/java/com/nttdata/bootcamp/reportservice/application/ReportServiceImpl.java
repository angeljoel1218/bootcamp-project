package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.application.utils.DateUtil;
import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CreditFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.reportservice.model.ProductConsumer;
import com.nttdata.bootcamp.reportservice.model.constan.ProductType;
import com.nttdata.bootcamp.reportservice.model.constan.TypeAffectation;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDuesDto;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
import java.math.BigDecimal;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @since 2022
 */

@Log4j2
@Service
public class ReportServiceImpl  implements ReportService {

  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  @Autowired
  CustomerFeignClient customerFeignClient;

  @Autowired
  AccountFeignClient accountFeignClient;

  @Autowired
  CreditFeignClient creditFeignClient;

  @Autowired
  ProductFeignClient productFeignClient;


  @Override
  public Flux<Map<String, Object>> dailyBalance(String customerId) {
      return    accountFeignClient.findAccountByHolderId(customerId).map(t -> {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Number Account", t.getNumber());
        map.put("balances per day", listTransactions(t.getId()));
        return map;
      });

  }

  @Override
  public Flux<Mono<Map<String, Object>>> productsCommissionByDates(Date startData,
                                                                   Date endDate,
                                                                   String customerId) {
      return  accountFeignClient.findAccountByHolderId(customerId).map(t -> {
        Map<String, Object> map = new  LinkedHashMap<>();
        map.put("Number Account", t.getNumber());
        return  accountFeignClient.findTransactionByAccountId(t.getId())
          .filter(cf -> cf.getCommission().compareTo(BigDecimal.ZERO) > 0
            && cf.getDate().after(startData)
            && cf.getDate().before(endDate)).collectList()
          .map(list -> {
            map.put("Commission", list);
            return map;
          });
      });
  }

  private Map<Date, BigDecimal> listTransactions(String accountId) {

    List<TransactionDto> transactions = new ArrayList<>();
    accountFeignClient.findTransactionByAccountId(accountId)
      .collectList().subscribe(transactions::addAll);

    Map<Date, BigDecimal> mapBalances = new LinkedHashMap<>();
    Date startDate = DateUtil.getStartOfMonth();
    Date endDate = DateUtil.getEndOfMonth();
    while (startDate.before(endDate)) {

        mapBalances.put(startDate, getSaldo(transactions, startDate, mapBalances));
        startDate = DateUtils.addDays(startDate, 1);

    }

    return mapBalances;
  }


  private static BigDecimal getSaldo(List<TransactionDto> transacciones,
                                     Date startDate,
                                     Map<Date, BigDecimal> mapSaldos ) {

    BigDecimal balanceBefore = mapSaldos.get(DateUtils.addDays(startDate, -1));

    BigDecimal balanceInitial = balanceBefore != null ?  balanceBefore :
      transacciones.stream().filter(t -> t.getDate().before(startDate))
        .map(t -> t.getAmount().multiply(getAfectation(t)))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal balanceDay = transacciones.stream()
      .filter(t -> DateUtils.truncatedEquals(t.getDate(), startDate, Calendar.DATE))
      .map(t -> t.getAmount().multiply((getAfectation(t))))
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return balanceInitial.add(balanceDay);
  }

  private static BigDecimal getAfectation(TransactionDto t) {
    return t.getAffectation() == TypeAffectation.DECREMENT
      ? BigDecimal.valueOf(-1) : BigDecimal.ONE;
  }

  @Override
    public Mono<CustomerDto> findCustomerById(String id) {
        return customerFeignClient.findCustomerById(id);
  }

  @Override
  public Flux<Map<String, Object>> findProductsByCustomer(String customerId) {
    return Flux.concat(

          /*Accounts*/
          accountFeignClient.findAccountByHolderId(customerId).map(account -> {
            Map<String, Object> objectMap = new LinkedHashMap<>();
            objectMap.put("Type", account.getTypeAccount());
            objectMap.put("Number", account.getNumber());
            objectMap.put("Balance", account.getBalance());
            return objectMap;
          }),

          /*Credits */
          creditFeignClient.findCreditByIdCustomer(customerId).map(credit -> {
            Map<String, Object> objectMap = new LinkedHashMap<>();
            objectMap.put("Type", "Credit");
            objectMap.put("Id", credit.getId());
            objectMap.put("Amount", credit.getAmountCredit());
            objectMap.put("Amount Payed", credit.getAmountPayed());
            return objectMap;
          }),

          /*Credit card*/
          creditFeignClient.findCreditCardByIdCustomer(customerId).map(credit -> {
            Map<String, Object> objectMap = new LinkedHashMap<>();
            objectMap.put("Type", "Credit Card");
            objectMap.put("Id", credit.getId());
            objectMap.put("Limit amount", credit.getLimitAmount());
            objectMap.put("Amount used", credit.getAmountUsed());
            return  objectMap;
          })

        );

  }

  @Override
  public Mono<ProductConsumer> findProductConsumerByDateBetween(String idProduct,
        Date startData,
        Date endDate) {

    return productFeignClient.findById(idProduct).flatMap(product -> {
          ProductConsumer pc = new  ProductConsumer();
          pc.setProductDto(product);
          Mono<ProductConsumer> mono  = null;

          if (Arrays.asList(ProductType.PERSONAL_CREDIT, ProductType.BUSINESS_CREDIT)
            .contains(product.getProductTypeId())) {

            mono = creditFeignClient.findCreditByCreateDateBetweenAndIdProduct(startData,
              endDate, idProduct).collectList().map(t -> {
              pc.setProducts(t);
              return pc;
            });

          }

          if (Arrays.asList(ProductType.PERSONAL_CREDIT_CARD, ProductType.BUSINESS_CREDIT_CARD)
            .contains(product.getProductTypeId())) {

            mono = creditFeignClient.findByCreditCardCreateDateBetweenAndIdProduct(startData,
              endDate, idProduct).collectList().map(t -> {
              pc.setProducts(t);
              return pc;
            });
          }

          if (Arrays.asList(ProductType.SAVINGS_ACCOUNT,
              ProductType.CURRENT_ACCOUNT, ProductType.FIXED_TERM_ACCOUNT)
            .contains(product.getProductTypeId())) {

            mono = accountFeignClient.findByCreateDateBetweenAndProductId(startData,
              endDate, idProduct).collectList().map(t -> {
              pc.setProducts(t);
              return pc;
            });
          }

          return mono;
        });
      }
}