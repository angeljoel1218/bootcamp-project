package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.infrastructure.AccountRepository;
import com.nttdata.bootcamp.productservice.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Mono<Account> create(Mono<Account> account) {
        return account.flatMap(accountRepository::insert);
    }

    @Override
    public Mono<Account> update(String id, Account account) {
        return this.findById(id).flatMap(bankAccount -> {
            bankAccount.setName(account.getName());
            bankAccount.setMaintenance(account.getMaintenance());
            bankAccount.setMaxMovements(account.getMaxMovements());
            bankAccount.setType(account.getType());
            bankAccount.setCoin(account.getCoin());
            return accountRepository.save(bankAccount);
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.findById(id).flatMap(accountRepository::delete);
    }

    @Override
    public Mono<Account> findById(String id) {
        return accountRepository.findById(id);
    }

    @Override
    public Flux<Account> findAll() {
        return accountRepository.findAll();
    }

}
