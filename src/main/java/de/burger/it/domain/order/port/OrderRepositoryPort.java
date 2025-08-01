package de.burger.it.domain.order.port;

import de.burger.it.domain.order.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Optional<Order> findById(UUID orderId);
    void save(Order order);
}