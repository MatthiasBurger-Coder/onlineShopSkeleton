package de.burger.it.adapter.relation;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import de.burger.it.domain.relation.model.CartOrderAssignment;
import de.burger.it.domain.relation.port.CartOrderAssignmentPort;
import de.burger.it.infrastructure.relation.port.CartOrderAssignmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CartOrderAssignmentAdapter implements CartOrderAssignmentPort {

    private final CartOrderAssignmentRepository repository;

    public CartOrderAssignmentAdapter(CartOrderAssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void assign(Cart cart, Order order) {
        var ownership =  new CartOrderAssignment( cart.id(), order.id());
        repository.save(ownership);
    }

    @Override
    public CartOrderAssignment findAll(UUID cartId) {
        return repository.findById(cartId);
    }
}

