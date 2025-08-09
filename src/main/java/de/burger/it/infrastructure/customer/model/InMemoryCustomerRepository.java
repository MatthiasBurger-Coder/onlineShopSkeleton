package de.burger.it.infrastructure.customer.model;

import de.burger.it.domain.customer.model.CustomerDefault;
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

    private final Map<UUID, CustomerDefault> store = new ConcurrentHashMap<>();

    @Override
    public Customer findById(UUID customerId) {
        return Optional.ofNullable(store.get(customerId))
                .map(c -> (Customer) c)
                .orElse(CustomerNullObject.getInstance());
    }

    @Override
    public void save(CustomerDefault customer) {
        store.put(customer.id(), customer);
    }

}

