package com.nttdata.bootcamp.reportservice.feignclients;

import com.nttdata.bootcamp.reportservice.feignclients.fallback.AccountFeignClientFallBack;
import com.nttdata.bootcamp.reportservice.model.dto.AccountDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(value =  "${feign.service.accounts.name}", fallback = AccountFeignClientFallBack.class)
public interface AccountFeignClient {

    @GetMapping("account/customer/{holderId}")
    public  Flux<AccountDto>  findAccountByHolderId (@PathVariable("holderId") String id);

    @GetMapping("account/transaction/{accountId}/list")
    public Flux<TransactionDto> findTransactionByAccountId (@PathVariable("accountId") String id);






}
