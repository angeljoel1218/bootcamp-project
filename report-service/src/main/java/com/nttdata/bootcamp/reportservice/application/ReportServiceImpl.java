package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.application.utils.DateUtil;
import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CreditFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.reportservice.model.ProductConsumer;
import com.nttdata.bootcamp.reportservice.model.constan.ProductType;
import com.nttdata.bootcamp.reportservice.model.constan.TypeAccount;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDto;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDuesDto;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.reportservice.model.constan.TypeAffectation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ReportServiceImpl  implements  ReportSevice{

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
          Map<String, Object> map = new  LinkedHashMap<>();
          map.put("Number Account", t.getNumber());
          map.put("balances per day", listTransactions(t.getId()));
          return map;
        });

  }
  @Override
  public Flux<Mono<Map<String, Object>>> productsCommissionByDates(Date startData, Date endDate, String customerId) {
    return  accountFeignClient.findAccountByHolderId(customerId).map(t -> {
      Map<String, Object> map = new  LinkedHashMap<>();
      map.put("Number Account",t.getNumber());
       return  accountFeignClient.findTransactionByAccountId(t.getId())
                .filter(cf -> cf.getCommission().compareTo(BigDecimal.ZERO) > 0 &&
                    cf.getDateOfTransaction().after(startData) &&
                    cf.getDateOfTransaction().before(endDate)).collectList()
                    .map(listCommisions->{
                        map.put("Commission",listCommisions);
                        return map;
         });
    });

  }

  private List<TransactionDto> listCommision(String accountId, Date startDay, Date endDate) {

    List<TransactionDto> transacciones = new ArrayList<>();
    accountFeignClient.findTransactionByAccountId(accountId).collectList().subscribe(transacciones::addAll);
    return transacciones.stream().filter(t -> t.getCommission().compareTo(BigDecimal.ZERO) > 0)
                .filter(t -> t.getDateOfTransaction().after(startDay) && t.getDateOfTransaction().before(endDate))
                .collect(Collectors.toList());

  }

  private Map<Date, BigDecimal> listTransactions(String accountId) {

    List<TransactionDto> transacciones = new ArrayList<>();
    accountFeignClient.findTransactionByAccountId(accountId).collectList().subscribe(transacciones::addAll);
    Map<Date, BigDecimal> mapSaldos = new LinkedHashMap<>();
    Date startDate = DateUtil.getStartOfMonth();
    Date endDate = DateUtil.getEndOfMonth();
    while (startDate.before(endDate)){

      mapSaldos.put(startDate, getSaldo(transacciones, startDate, mapSaldos));
      startDate = DateUtils.addDays(startDate, 1);

    }

    return mapSaldos;
  }


  private  List<CreditDuesDto> listDuesCredit(String idCredit) {
    List<CreditDuesDto> list  =  new ArrayList<>();
    creditFeignClient.findCreditDuesByIdCredit(idCredit).collectList().subscribe(list::addAll);
    return list;
  }


  private static BigDecimal getSaldo(List<TransactionDto> transacciones, Date startDate, Map<Date, BigDecimal> mapSaldos ) {

    BigDecimal saldoAnterior = mapSaldos.get(DateUtils.addDays(startDate, -1));

    BigDecimal saldoInicial=  saldoAnterior != null ?  saldoAnterior :
                                                    transacciones.stream().filter(t -> t.getDateOfTransaction().before(startDate))
                                                            .map(t -> t.getAmount().multiply((t.getAffectation() == TypeAffectation.DECREMENT ? BigDecimal.valueOf(-1) : BigDecimal.ONE)))
                                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoDia = transacciones.stream().filter(t-> DateUtils.truncatedEquals(t.getDateOfTransaction(),startDate,Calendar.DATE))
                .map(t -> t.getAmount().multiply((t.getAffectation() == TypeAffectation.DECREMENT ? BigDecimal.valueOf(-1) : BigDecimal.ONE)))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        return saldoInicial.add(saldoDia) ;
    }




    @Override
    public Mono<CustomerDto> findCustomerById(String id) {
        return customerFeignClient.findCustomerById(id);
    }

    @Override
    public Flux<Map<String,Object>> findProductsByCustomer(String customerId) {
      return Flux.concat(

        /*Accounts*/
         accountFeignClient.findAccountByHolderId(customerId).map(account -> {
           Map<String,Object> objectMap = new LinkedHashMap<>();
           objectMap.put("Type", account.getTypeAccount());
           objectMap.put("Number", account.getNumber());
           objectMap.put("Balance", account.getBalance());
           return objectMap ;
         }),

         /*Credits */

         creditFeignClient.findCreditByIdCustomer(customerId).map(credit -> {
           Map<String,Object> objectMap = new LinkedHashMap<>();
           objectMap.put("Type", "Credit");
           objectMap.put("Id", credit.getId());
           objectMap.put("Amount", credit.getAmountCredit());
           objectMap.put("Amount Payed", credit.getAmountPayed());
           return objectMap ;
         }),

        /*Credit card*/
          creditFeignClient.findCreditCardByIdCustomer(customerId).map(credit -> {
            Map<String,Object> objectMap = new LinkedHashMap<>();
            objectMap.put("Type", "Credit Card");
            objectMap.put("Id", credit.getId());
            objectMap.put("Limit amount", credit.getLimitAmount());
            objectMap.put("Amount used", credit.getAmountUsed());
            return  objectMap;
          })

         );

    }
    @Override
    public Mono<ProductConsumer> findProductConsumerByDateBetween(String idProduct, Date startData, Date endDate) {
        return productFeignClient.findById(idProduct).flatMap(product->{
            ProductConsumer pc= new  ProductConsumer();
            pc.setProductDto(product);

            Mono<ProductConsumer> mono  = null;

            if(Arrays.asList(ProductType.PERSONAL_CREDIT, ProductType.BUSINESS_CREDIT).contains(product.getProductTypeId())){
                mono = creditFeignClient.findCreditByCreateDateBetweenAndIdProduct(startData,endDate,idProduct)
                    .collectList().map(t-> {
                        pc.setProducts(t);
                        return pc;
                    });
            }

            if(Arrays.asList(ProductType.PERSONAL_CREDIT_CARD, ProductType.BUSINESS_CREDIT_CARD).contains(product.getProductTypeId())){
                mono = creditFeignClient.findByCreditCardCreateDateBetweenAndIdProduct(startData,endDate,idProduct)
                    .collectList().map(t-> {
                        pc.setProducts(t);
                        return pc;
                    });
            }

            if(Arrays.asList(ProductType.SAVINGS_ACCOUNT, ProductType.CURRENT_ACCOUNT, ProductType.FIXED_TERM_ACCOUNT).contains(product.getProductTypeId())){
                mono =Mono.just(new ProductConsumer());
            }


            return mono;
        });
    }

    @Override
    public Flux<CreditDto> find(String idProduct, Date startData, Date endDate) {
      log.info("toy aca");
        return   creditFeignClient.findCreditByCreateDateBetweenAndIdProduct(startData,endDate,idProduct);
    }
}
