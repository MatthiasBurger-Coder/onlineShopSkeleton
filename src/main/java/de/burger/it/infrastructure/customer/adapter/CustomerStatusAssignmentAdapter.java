package de.burger.it.infrastructure.customer.adapter;

import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.customer.model.CustomerStatusAssignment;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerStatusAssignmentAdapter implements CustomerStatusAssignmentPort {

    private final Map<UUID, CustomerStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public CustomerStateType findBy(UUID customerId) {
        UUID id = Optional.ofNullable(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer ID cannot be null"));
        return Optional.ofNullable(store.get(id))
                .map(CustomerStatusAssignment::state)
                .orElse(null);
    }

    @Override
    public void assign(CustomerDefault customer, CustomerStateType newState) {
        CustomerDefault nonNullCustomer = Optional.ofNullable(customer)
                .orElseThrow(() -> new IllegalArgumentException("Customer cannot be null"));
        CustomerStateType state = Optional.ofNullable(newState)
                .orElseThrow(() -> new IllegalArgumentException("State cannot be null"));
        var assignment = new CustomerStatusAssignment(
                nonNullCustomer.id(),
                state
        );
        store.put(assignment.customerId(), assignment);
    }
}