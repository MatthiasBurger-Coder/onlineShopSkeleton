package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.CartDefault;
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
    public void assign(CartDefault cart, Customer customer) {
        CartDefault nonNullCart = Optional.ofNullable(cart)
                .orElseThrow(() -> new IllegalArgumentException("Cart cannot be null"));
        Customer nonNullCustomer = Optional.ofNullable(customer)
                .orElseThrow(() -> new IllegalArgumentException("Customer cannot be null"));
        var assignment = new CartCustomerAssignment(
                nonNullCart.id(),
                nonNullCustomer.id()
        );
        store.computeIfAbsent(assignment.customerId(), k -> new CopyOnWriteArrayList<>()).add(assignment);
    }

    @Override
    public List<CartCustomerAssignment> findAllByCart(UUID cartId) {
        UUID id = Optional.ofNullable(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart ID cannot be null"));
        return store.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.cartId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartCustomerAssignment> findAllByCustomer(UUID customerId) {
        UUID id = Optional.ofNullable(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer ID cannot be null"));
        return store.getOrDefault(id, Collections.emptyList());
    }
}