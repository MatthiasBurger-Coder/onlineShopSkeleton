package de.burger.it.infrastructure.relation.port;

import de.burger.it.domain.relation.model.CartOrderAssignment;

import java.util.UUID;

public interface CartOrderAssignmentRepository {
    CartOrderAssignment findById(UUID orderId);

    void save(CartOrderAssignment cartCustomerAssignment);
}
