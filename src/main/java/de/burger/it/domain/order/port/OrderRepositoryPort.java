package de.burger.it.domain.order.port;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderLike;

import java.util.UUID;

public interface OrderRepositoryPort {
    OrderLike findById(UUID orderId);
    void save(Order order);
}