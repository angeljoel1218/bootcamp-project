package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("account")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> create(@RequestBody Account account) {
        return accountService.create(Mono.just(account));
    }

    @PutMapping("account/{id}")
    public Mono<Account> update(@RequestBody Account account, @PathVariable String id) {
        return accountService.update(id, account);
    }

    @DeleteMapping("account/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return accountService.delete(id);
    }

    @GetMapping("account/{id}")
    public Mono<Account> findById(@PathVariable String id) {
        return accountService.findById(id);
    }

    @GetMapping("account/all")
    public Flux<Account> findAll() {
        return accountService.findAll();
    }
}
