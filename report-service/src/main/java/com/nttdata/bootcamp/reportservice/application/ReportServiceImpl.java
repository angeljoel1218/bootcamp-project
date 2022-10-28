package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CreditFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CustomerFeignClient;
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
  @Override
  public Flux<Map<String, Object>> dailyBalance(String customerId) {
    return Flux.concat(
      accountFeignClient.findByHolderId(customerId).map(t -> {
          Map<String, Object> map = new  LinkedHashMap<>();
          map.put("Number Account", t.getNumber());
          map.put("balances per day", listTransactions(t.getId()));
          return map;
        }),

      creditFeignClient.findByIdCustomer(customerId).map(t -> {
          Map<String, Object> map = new  LinkedHashMap<>();
          map.put("Id credit ", t.getId());
          map.put("Dues Credit", listDuesCredit(t.getId()));
          return map;
        })
      );
  }
  @Override
  public Flux<Map<String, Object>> productsCommissionByDates(Date startData, Date endDate, String customerId) {

    return  accountFeignClient.findByHolderId(customerId).map(t -> {
      Map<String, Object> map = new  LinkedHashMap<>();
      map.put("Number Account",t.getNumber());
      map.put("Commission", listCommision(t.getId(), startData, endDate));
      return map;
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
    Date startDate = getStartOfMonth();
    Date endDate = getEndOfMonth();
    while (startDate.before(endDate)){

      mapSaldos.put(startDate, getSaldo(transacciones, startDate, mapSaldos));
      startDate = DateUtils.addDays(startDate, 1);

    }

    return mapSaldos;
  }


  private  List<CreditDuesDto> listDuesCredit(String idCredit) {

    List<CreditDuesDto> creditDuesDtos = new ArrayList<>();
    creditFeignClient.findCreditDuesByIdCredit(idCredit).collectList().subscribe(creditDuesDtos::addAll);

    return creditDuesDtos;
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

    public static Date getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }



    @Override
    public Mono<CustomerDto> findCustomerById(String id) {
        return customerFeignClient.findById(id);
    }
}
