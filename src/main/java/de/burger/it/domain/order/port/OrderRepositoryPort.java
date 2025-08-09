package de.burger.it.domain.order.port;

import de.burger.it.domain.order.model.Order;

import java.util.UUID;

public interface OrderRepositoryPort {
    Order findById(UUID orderId);
    void save(Order order);
}