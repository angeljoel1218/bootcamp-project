package com.nttdata.bootcamp.productservice.infrastructure;

import com.nttdata.bootcamp.productservice.model.ProductAccount;
import com.nttdata.bootcamp.productservice.model.TypeAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductAccountRepository extends ReactiveMongoRepository<ProductAccount, String> {
    Mono<ProductAccount> findByType(TypeAccount type);
}
