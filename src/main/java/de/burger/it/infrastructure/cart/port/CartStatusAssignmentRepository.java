package de.burger.it.infrastructure.cart.port;

import de.burger.it.domain.cart.model.CartStatusAssignment;

import java.util.UUID;

public interface CartStatusAssignmentRepository {

    void save(CartStatusAssignment assignment);

    CartStatusAssignment findAllById(UUID cartId);
}
