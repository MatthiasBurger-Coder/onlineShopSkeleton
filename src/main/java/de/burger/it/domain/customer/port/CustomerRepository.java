package de.burger.it.domain.customer.port;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerLike;

import java.util.UUID;

public interface CustomerRepository {
    CustomerLike findById(UUID customerId);
    void save(Customer customer);
}