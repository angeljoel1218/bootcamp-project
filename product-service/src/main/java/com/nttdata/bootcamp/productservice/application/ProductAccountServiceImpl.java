package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.application.mappers.MapperProductAccount;
import com.nttdata.bootcamp.productservice.infrastructure.ProductAccountRepository;
import com.nttdata.bootcamp.productservice.model.ProductAccount;
import com.nttdata.bootcamp.productservice.model.dto.ProductAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductAccountServiceImpl implements ProductAccountService {
    @Autowired
    MapperProductAccount mapperProductAccount;
    @Autowired
    ProductAccountRepository productAccountRepository;

    @Override
    public Mono<ProductAccountDto> create(ProductAccountDto account) {
        Mono<ProductAccount> productAccount = mapperProductAccount.toProductAccount(account);
        Mono<ProductAccount> productAccountMono = productAccount.flatMap(productAccountRepository::insert);
        return productAccountMono.flatMap(mapperProductAccount::toDto);
    }

    @Override
    public Mono<ProductAccountDto> update(String id, ProductAccountDto productAccount) {
        Mono<ProductAccount> foundProductAccount = productAccountRepository.findById(id);
        Mono<ProductAccount> upProductAccount = foundProductAccount.flatMap(bankProductAccount -> {
            bankProductAccount.setName(productAccount.getName());
            bankProductAccount.setMaintenance(productAccount.getMaintenance());
            bankProductAccount.setMaxMovements(productAccount.getMaxMovements());
            bankProductAccount.setType(productAccount.getType());
            bankProductAccount.setCoin(productAccount.getCoin());
            return productAccountRepository.save(bankProductAccount);
        });
        return upProductAccount.flatMap(mapperProductAccount::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        Mono<ProductAccount> foundProductAccount = productAccountRepository.findById(id);
        return foundProductAccount.flatMap(productAccountRepository::delete);
    }

    @Override
    public Mono<ProductAccountDto> findById(String id) {
        return productAccountRepository.findById(id)
                .flatMap(mapperProductAccount::toDto);
    }

    @Override
    public Flux<ProductAccountDto> findAll() {
        return productAccountRepository.findAll()
                .flatMap(mapperProductAccount::toDto);
    }

}
