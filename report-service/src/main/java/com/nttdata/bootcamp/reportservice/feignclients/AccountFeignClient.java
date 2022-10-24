package com.nttdata.bootcamp.reportservice.feignclients;

import com.nttdata.bootcamp.reportservice.model.CurrentAccountDto;
import com.nttdata.bootcamp.reportservice.model.FixedTermDepositAccountDto;
import com.nttdata.bootcamp.reportservice.model.TransactionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value =  "${feign.service.accounts.name}")
public interface AccountFeignClient {

    @GetMapping("current-account/customer/{holderId}")
    public  Flux<CurrentAccountDto>  findCurrentByHolderId (@PathVariable("holderId") String id);

    @GetMapping("current-account/{accountId}/transactions")
    public Flux<TransactionDto> listCurrentTransactions (@PathVariable("accountId") String id);


    @GetMapping("fixed-term-deposit/customer/{holderId}")
    public  Mono<FixedTermDepositAccountDto>  findFixedByHolderId (@PathVariable("holderId") String id);




}
