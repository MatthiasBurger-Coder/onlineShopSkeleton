package de.burger.it.infrastructure.order.model;

import de.burger.it.domain.order.model.OrderNullObject;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderLike;
import de.burger.it.domain.order.port.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository implements OrderRepositoryPort {

    private final Map<UUID, Order> store = new ConcurrentHashMap<>();

    @Override
    public OrderLike findById(UUID orderId) {
        return Optional.ofNullable(store.get(orderId))
                .map(o -> (OrderLike) o)
                .orElse(OrderNullObject.getInstance());
    }

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }
}
