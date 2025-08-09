package de.burger.it.domain.customer.port;

import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.customer.state.CustomerStateType;

import java.util.UUID;

public interface CustomerStatusAssignmentPort {

    CustomerStateType findBy(UUID customerId);

    void assign(CustomerDefault customer, CustomerStateType newState);
}
