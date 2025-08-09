package de.burger.it.infrastructure.cart.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartStatusAssignment;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CartStatusAssignmentAdapter implements CartStatusAssignmentPort {
    private final Map<UUID, CartStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public void assign(Cart cart, CartStateType newState) {
        var nonNullCart = Optional.ofNullable(cart)
                .orElseThrow(() -> new IllegalArgumentException("Cart cannot be null"));
        var state = Optional.ofNullable(newState)
                .orElseThrow(() -> new IllegalArgumentException("State cannot be null"));
        var assignment = new CartStatusAssignment(
                nonNullCart.id(),
                state,
                LocalDateTime.now(),
                null
        );
        store.put(assignment.cartId(), assignment);
    }

    @Override
    public CartStateType findBy(UUID cartId) {
        var id = Optional.ofNullable(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart ID cannot be null"));
        return Optional.ofNullable(store.get(id))
                .map(CartStatusAssignment::state)
                .orElse(null);
    }
}