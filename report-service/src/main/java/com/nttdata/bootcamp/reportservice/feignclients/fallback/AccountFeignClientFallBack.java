package com.nttdata.bootcamp.reportservice.feignclients.fallback;

import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.model.CurrentAccountDto;
import com.nttdata.bootcamp.reportservice.model.FixedTermDepositAccountDto;
import com.nttdata.bootcamp.reportservice.model.TransactionDto;
import com.nttdata.bootcamp.reportservice.model.TypeTransaction;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.time.DateUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
public class AccountFeignClientFallBack implements AccountFeignClient {
    @Override
    public Flux<CurrentAccountDto> findCurrentByHolderId(String id) {
        List<CurrentAccountDto> dts= new ArrayList<>();
        CurrentAccountDto ss = new CurrentAccountDto();
        ss.setId("accounUd");
        ss.setNumber("Numero");


        CurrentAccountDto ss2 = new CurrentAccountDto();
        ss2.setId("accounUd");
        ss2.setNumber("05343434");

        dts.add(ss);
        dts.add(ss2);

        return Flux.fromIterable(dts);
    }

    @Override
    public Flux<TransactionDto> listCurrentTransactions(String id) {
        List<TransactionDto> dts= new ArrayList<>();

        TransactionDto d1I= new TransactionDto("111", "Deposit",BigDecimal.valueOf(100),"accounUd","Dest",new Date(), TypeTransaction.INCOMING,BigDecimal.ZERO);
        TransactionDto d2I= new TransactionDto("111", "Deposit",BigDecimal.valueOf(100),"accounUd","Dest", DateUtils.addDays(new Date(),1), TypeTransaction.INCOMING,BigDecimal.ZERO);


        TransactionDto d1O= new TransactionDto("111", "Deposit",BigDecimal.valueOf(50),"accounUd","Dest",new Date(), TypeTransaction.OUTGOING,BigDecimal.ONE);
        TransactionDto d2O= new TransactionDto("111", "Deposit",BigDecimal.valueOf(20),"accounUd","Dest", DateUtils.addDays(new Date(),1), TypeTransaction.OUTGOING,BigDecimal.ONE);


        dts.add(d1I);
        dts.add(d2I);
        dts.add(d1O);
        dts.add(d2O);

        log.info("servicio no disponible datos de prueba");

        return Flux.fromIterable(dts);
    }

    @Override
    public Mono<FixedTermDepositAccountDto> findFixedByHolderId(String id) {
        return null;
    }
}
