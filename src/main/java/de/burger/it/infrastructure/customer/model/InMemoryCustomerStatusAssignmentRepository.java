package de.burger.it.infrastructure.customer.model;

import de.burger.it.domain.customer.model.CustomerStatusAssignment;
import de.burger.it.infrastructure.customer.port.CustomerStatusAssignmentRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerStatusAssignmentRepository implements CustomerStatusAssignmentRepository {

    private final Map<UUID, CustomerStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public void save(CustomerStatusAssignment assignment) {
        store.put(assignment.customerId(), assignment);
    }

    @Override
    public CustomerStatusAssignment findBy(UUID customerId) {
        return store.get(customerId);
    }
}
