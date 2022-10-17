package com.nttdata.bootcamp.productservice.infrastructure;

import com.nttdata.bootcamp.productservice.model.ProductAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductAccountRepository extends ReactiveMongoRepository<ProductAccount, String> {
}
