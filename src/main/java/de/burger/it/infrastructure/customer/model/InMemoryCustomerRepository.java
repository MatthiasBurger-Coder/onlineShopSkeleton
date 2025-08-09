package de.burger.it.infrastructure.customer.model;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerNullObject;
import de.burger.it.domain.customer.port.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<UUID, Customer> store = new ConcurrentHashMap<>();

    @Override
    public Customer findById(UUID customerId) {
        return Optional.ofNullable(store.get(customerId))
                .orElse(CustomerNullObject.getInstance());
    }

    @Override
    public void save(Customer customer) {
        store.put(customer.id(), customer);
    }

}

