package com.nttdata.bootcamp.reportservice.application;

import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.reportservice.model.CurrentAccountDto;
import com.nttdata.bootcamp.reportservice.model.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.TypeTransaction;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class ReportServiceImpl  implements  ReportSevice{


    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    CustomerFeignClient customerFeignClient;

    @Autowired
    AccountFeignClient accountFeignClient;

    @Override
    public Flux<CurrentAccountDto> dailyBalance(String customerId) {
        return accountFeignClient.findCurrentByHolderId(customerId);

    }

    @Override
    public Flux<Map<String, Object>> dailyBalance2(String accountId) {
        Map<String,Object> map= new HashMap<>();
        return accountFeignClient.listCurrentTransactions(accountId).map(tran->{
            String fecha= dateFormat.format(tran.getDateOfTransaction());

            BigDecimal monto= BigDecimal.ZERO;
            if(map.containsKey(fecha)){
                monto= (BigDecimal) map.get(fecha);
            }

            map.put(fecha, monto.add(
                    (tran.getType()== TypeTransaction.OUTGOING ? new BigDecimal(-1)  : BigDecimal.ONE).multiply(tran.getAmount())
            ));


            return map;
        });
    }

    /*
    private Map<String, Object> getMapFlux(CurrentAccountDto account, Map<String, Object> map) {
        Flux<Map<String,Object>> dias=    accountFeignClient.listCurrentTransactions(account.getId()).map(tran->{



             BigDecimal monto= BigDecimal.ZERO;
             if(map.containsKey(fecha)){
                 monto= (BigDecimal) map.get(fecha);
             }

            map.put(fecha, monto.add(
                     (tran.getType()== TypeTransaction.OUTGOING ? new BigDecimal(-1)  : BigDecimal.ONE).multiply(tran.getAmount())
                     ));

             return map;
          });
        return dias.blockLast();
    }
    */

    @Override
    public Mono<CustomerDto> findCustomerById(String id) {
        return customerFeignClient.findById(id);
    }
}
