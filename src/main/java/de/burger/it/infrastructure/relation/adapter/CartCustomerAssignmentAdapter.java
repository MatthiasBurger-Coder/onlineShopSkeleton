package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class CartCustomerAssignmentAdapter implements CartCustomerAssignmentPort {

    private final Map<UUID, List<CartCustomerAssignment>> store = new ConcurrentHashMap<>();

    @Override
    public void assign(Cart cart, Customer customer) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        var assignment = new CartCustomerAssignment(
                cart.id(),
                customer.id()
        );
        store.computeIfAbsent(assignment.customerId(), k -> new CopyOnWriteArrayList<>()).add(assignment);
    }

    @Override
    public List<CartCustomerAssignment> findAllByCart(UUID cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        return store.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.cartId().equals(cartId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartCustomerAssignment> findAllByCustomer(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        return store.getOrDefault(customerId, Collections.emptyList());
    }
}