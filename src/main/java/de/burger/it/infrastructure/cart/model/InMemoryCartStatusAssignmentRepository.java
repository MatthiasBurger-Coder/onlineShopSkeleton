package de.burger.it.infrastructure.cart.model;

import de.burger.it.infrastructure.cart.port.CartStatusAssignmentRepository;
import de.burger.it.domain.cart.model.CartStatusAssignment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCartStatusAssignmentRepository implements CartStatusAssignmentRepository {
    private final Map<UUID, CartStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public void save(CartStatusAssignment assignment) {
        store.put(assignment.cartId(), assignment);
    }

    @Override
    public CartStatusAssignment findAllById(UUID cartId) {
        return store.get(cartId);
    }

}
