package de.burger.it.domain.relation.port;

import de.burger.it.domain.relation.model.CartOrderAssignment;

import java.util.UUID;

public interface CartOrderAssignmentRepositoryPort {
    CartOrderAssignment findById(UUID orderId);

    void save(CartOrderAssignment cartOrderAssignment);
}