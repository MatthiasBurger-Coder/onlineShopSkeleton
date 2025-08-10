package de.burger.it.domain.customer.port;

import de.burger.it.domain.customer.model.Customer;

import java.util.UUID;

public interface CustomerRepositoryPort {
    Customer findById(UUID customerId);

    void save(Customer customer);
}