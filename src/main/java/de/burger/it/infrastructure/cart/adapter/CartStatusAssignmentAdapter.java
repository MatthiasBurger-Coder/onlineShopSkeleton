package de.burger.it.infrastructure.cart.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartStatusAssignment;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartStateType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class CartStatusAssignmentAdapter implements CartStatusAssignmentPort {
    private final Map<UUID, CartStatusAssignment> store = new ConcurrentHashMap<>();

    @Override
    public void assign(Cart cart, CartStateType newState) {
        var assignment = new CartStatusAssignment(
                cart.id(),
                newState,
                LocalDateTime.now(),
                null
        );
        store.put(assignment.cartId(), assignment);
    }

    @Override
    public CartStateType findBy(UUID cartId) {
        return store.get(cartId).state();
    }
}