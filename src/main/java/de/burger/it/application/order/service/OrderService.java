package de.burger.it.application.order.service;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.port.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
    }

    public Order createNewOrder(Cart cart) {
        var order = new Order(UUID.randomUUID());
        this.orderRepository.save(order);
        return order;
    }
}
