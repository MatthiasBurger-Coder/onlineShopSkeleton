package de.burger.it.infrastructure.customer.adapter;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerStatusAssignment;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerStatusAssignmentAdapter implements CustomerStatusAssignmentPort {

    private final Map<UUID, CustomerStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public CustomerStateType findBy(UUID customerId) {
        return store.get(customerId).state();
    }

    @Override
    public void assign(Customer customer, CustomerStateType newState) {
        var assignment = new CustomerStatusAssignment(
                customer.id(),
                newState
        );
        store.put(assignment.customerId(), assignment);
    }
}