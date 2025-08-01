package de.burger.it.application.order.service;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.port.OrderRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepositoryPort orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepositoryPort orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public Order createNewOrder(Cart cart) {
        var order = new Order(UUID.randomUUID());
        eventPublisher.publishEvent(new OrderCreateEvent(order));
        return order;
    }
    
    public void payOrder(Order order) {
        eventPublisher.publishEvent(new OrderPayEvent(order));
    }
    
    public void cancelOrder(Order order) {
        eventPublisher.publishEvent(new OrderCancelEvent(order));
    }
    
    public void deliverOrder(Order order) {
        eventPublisher.publishEvent(new OrderDeliverEvent(order));
    }
}
