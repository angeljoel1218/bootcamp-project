package com.nttdata.bootcamp.productservice.infrastructure;

import com.nttdata.bootcamp.productservice.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Some javadoc.
 * @since 2022
 */
@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
