package de.burger.it.infrastructure.customer.model;

import de.burger.it.infrastructure.customer.port.CustomerRepository;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerLike;
import de.burger.it.domain.customer.model.NullCustomer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<UUID, Customer> store = new ConcurrentHashMap<>();

    @Override
    public CustomerLike findById(UUID customerId) {
        Customer customer = store.get(customerId);
        return customer != null ? customer : NullCustomer.getInstance();
    }

    @Override
    public void save(Customer customer) {
        store.put(customer.id(), customer);
    }

}

