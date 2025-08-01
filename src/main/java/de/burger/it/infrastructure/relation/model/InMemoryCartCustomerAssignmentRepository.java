package de.burger.it.infrastructure.relation.model;


import de.burger.it.infrastructure.relation.port.CartCustomerAssignmentRepository;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryCartCustomerAssignmentRepository implements CartCustomerAssignmentRepository {

    private final Map<UUID, List<CartCustomerAssignment>> store = new ConcurrentHashMap<>();

    @Override
    public void save(CartCustomerAssignment assignment) {
        store.computeIfAbsent(assignment.customerId(), k -> new ArrayList<>()).add(assignment);
    }

    @Override
    public List<CartCustomerAssignment> findAllByCustumerId(UUID customerId) {
        return store.getOrDefault(customerId, Collections.emptyList());
    }

    @Override
    public List<CartCustomerAssignment> findAllByCartId(UUID cartId) {
        return store.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.cartId().equals(cartId))
                .collect(Collectors.toList());
    }
}

