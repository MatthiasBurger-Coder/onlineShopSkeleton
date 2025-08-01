package de.burger.it.infrastructure.relation.adapter;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.relation.model.CartOrderAssignment;
import de.burger.it.domain.relation.port.CartOrderAssignmentPort;
import de.burger.it.domain.relation.port.CartOrderAssignmentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CartOrderAssignmentAdapter implements CartOrderAssignmentPort {

    private final CartOrderAssignmentRepositoryPort repository;

    public CartOrderAssignmentAdapter(CartOrderAssignmentRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void assign(Cart cart, Order order) {
        var ownership = new CartOrderAssignment(cart.id(), order.id());
        repository.save(ownership);
    }

    @Override
    public CartOrderAssignment findAll(UUID cartId) {
        return repository.findById(cartId);
    }
}