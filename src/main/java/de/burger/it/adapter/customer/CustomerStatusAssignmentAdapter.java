package de.burger.it.adapter.customer;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerStatusAssignment;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.infrastructure.customer.port.CustomerStatusAssignmentRepository;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerStatusAssignmentAdapter implements CustomerStatusAssignmentPort {

    private final CustomerStatusAssignmentRepository repository;

    public CustomerStatusAssignmentAdapter(CustomerStatusAssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerStateType findBy(UUID customerId) {
        return repository.findBy(customerId).state();
    }

    @Override
    public void assign(Customer cart, CustomerStateType newState) {
        var assignment = new CustomerStatusAssignment(
                cart.id(),
                newState
        );
        repository.save(assignment);
    }
}
