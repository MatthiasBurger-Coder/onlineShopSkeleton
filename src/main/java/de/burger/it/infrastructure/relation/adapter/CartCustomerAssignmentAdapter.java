package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CartCustomerAssignmentAdapter implements CartCustomerAssignmentPort {

    private final Map<UUID, List<CartCustomerAssignment>> store = new ConcurrentHashMap<>();

    @Override
    public void assign(Cart cart, Customer customer) {
        var assignment = new CartCustomerAssignment(
                cart.id(),
                customer.id()
        );
        store.computeIfAbsent(assignment.customerId(), k -> new ArrayList<>()).add(assignment);
    }

    @Override
    public List<CartCustomerAssignment> findAllByCard(UUID cartId) {
        return store.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.cartId().equals(cartId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartCustomerAssignment> findAllByCustomer(UUID customerId) {
        return store.getOrDefault(customerId, Collections.emptyList());
    }
}