package com.nttdata.bootcamp.productservice.infrastructure;

import com.nttdata.bootcamp.productservice.model.ProductCredit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductCreditRepository extends ReactiveMongoRepository<ProductCredit, String> {
}
