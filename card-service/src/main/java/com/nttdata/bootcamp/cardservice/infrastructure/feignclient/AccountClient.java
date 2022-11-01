package com.nttdata.bootcamp.cardservice.infrastructure.feignclient;

import com.nttdata.bootcamp.cardservice.model.dto.AccountDto;
import com.nttdata.bootcamp.cardservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.cardservice.model.dto.WithdrawTxnDto;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "${feign.service.account.name}")
public interface AccountClient {
    @RequestMapping(method = RequestMethod.GET, value = "account/customer/{holderId}")
    Flux<AccountDto> findByHolderId(@PathVariable("holderId") String holderId);

    @RequestMapping(method = RequestMethod.GET, value = "account/{number}")
    Mono<AccountDto> findByNumber(@PathVariable("number") String number);

    @RequestMapping(method = RequestMethod.PUT, value = "account/transaction/withdraw")
    Mono<TransactionDto> withdraw(@RequestBody WithdrawTxnDto withdrawDto);
}
