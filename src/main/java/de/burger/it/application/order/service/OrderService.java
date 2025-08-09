package de.burger.it.application.order.service;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.model.OrderNullObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {

        this.eventPublisher = eventPublisher;
    }

    public Order createNewOrder(Cart cart) {
        if (cart == null || cart.isNull()) {
            return OrderNullObject.getInstance();
        }
        var order = new OrderDefault(UUID.randomUUID());
        eventPublisher.publishEvent(new OrderCreateEvent(order));
        return order;
    }

    public void payOrder(Order order) {
        if (order == null || order.isNull()) {
            return;
        }
        eventPublisher.publishEvent(new OrderPayEvent(order));
    }

    public void cancelOrder(Order order) {
        if (order == null || order.isNull()) {
            return;
        }
        eventPublisher.publishEvent(new OrderCancelEvent(order));
    }

    public void deliverOrder(Order order) {
        if (order == null || order.isNull()) {
            return;
        }
        eventPublisher.publishEvent(new OrderDeliverEvent(order));
    }
}
