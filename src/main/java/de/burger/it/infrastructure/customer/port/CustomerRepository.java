package de.burger.it.infrastructure.customer.port;

import de.burger.it.domain.customer.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findById(UUID customerId);
    void save(Customer customer);

}
