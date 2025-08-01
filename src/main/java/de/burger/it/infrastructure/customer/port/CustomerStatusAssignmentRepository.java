package de.burger.it.infrastructure.customer.port;

import de.burger.it.domain.customer.model.CustomerStatusAssignment;

import java.util.UUID;

public interface CustomerStatusAssignmentRepository {

    void save(CustomerStatusAssignment assignment);

    CustomerStatusAssignment findBy(UUID customerId);
}
