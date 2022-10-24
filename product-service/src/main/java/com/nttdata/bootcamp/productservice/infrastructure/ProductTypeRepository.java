package com.nttdata.bootcamp.productservice.infrastructure;

import com.nttdata.bootcamp.productservice.model.ProductType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductTypeRepository extends ReactiveMongoRepository<ProductType, String> {
}
