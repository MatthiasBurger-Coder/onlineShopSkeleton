package de.burger.it.domain.relation.port;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.relation.model.CartOrderAssignment;

import java.util.UUID;

public interface CartOrderAssignmentPort {

    void assign(Cart cart, Order order);

    CartOrderAssignment findAll(UUID cartId);
}
