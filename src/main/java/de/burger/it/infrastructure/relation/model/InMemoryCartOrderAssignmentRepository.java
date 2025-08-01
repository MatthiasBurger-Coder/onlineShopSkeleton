package de.burger.it.infrastructure.relation.model;


import de.burger.it.domain.relation.model.CartOrderAssignment;
import de.burger.it.domain.relation.port.CartOrderAssignmentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCartOrderAssignmentRepository implements CartOrderAssignmentRepositoryPort {

    private final Map<UUID, CartOrderAssignment> store = new ConcurrentHashMap<>();


    @Override
    public CartOrderAssignment findById(UUID orderId) {
        return store.get(orderId);
    }

    @Override
    public void save(CartOrderAssignment cartCustomerAssignment) {
        store.put(cartCustomerAssignment.orderId(), cartCustomerAssignment);
    }

}

