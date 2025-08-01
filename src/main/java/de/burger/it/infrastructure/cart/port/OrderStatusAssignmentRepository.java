package de.burger.it.infrastructure.cart.port;

import de.burger.it.domain.order.model.OrderStatusAssignment;

import java.util.UUID;

public interface OrderStatusAssignmentRepository {

    void save(OrderStatusAssignment assignment);

    OrderStatusAssignment findBy(UUID orderId);
}
