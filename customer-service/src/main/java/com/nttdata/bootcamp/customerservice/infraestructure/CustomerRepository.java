package com.nttdata.bootcamp.customerservice.infraestructure;

import com.nttdata.bootcamp.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */
@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
