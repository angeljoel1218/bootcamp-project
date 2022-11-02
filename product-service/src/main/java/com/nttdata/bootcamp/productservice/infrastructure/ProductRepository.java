package com.nttdata.bootcamp.productservice.infrastructure;

import com.nttdata.bootcamp.productservice.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


/**
 * javadoc.
 * Product repository
 * @since 2022
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
