package de.burger.it.infrastructure.customer.model;

import de.burger.it.infrastructure.customer.port.CustomerRepository;
import de.burger.it.domain.customer.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<UUID, Customer> store = new HashMap<>();

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return Optional.ofNullable(store.get(customerId));
    }

    @Override
    public void save(Customer customer) {
        store.put(customer.id(), customer);
    }

}

