package de.burger.it.application.order.service;

import de.burger.it.domain.cart.model.CartLike;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.model.NullOrder;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderLike;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {

        this.eventPublisher = eventPublisher;
    }

    public OrderLike createNewOrder(CartLike cart) {
        if (cart.isNull()) {
            return NullOrder.getInstance();
        }
        var order = new Order(UUID.randomUUID());
        eventPublisher.publishEvent(new OrderCreateEvent(order));
        return order;
    }

    public void payOrder(OrderLike order) {
        if (order.isNull()) {
            return;
        }
        eventPublisher.publishEvent(new OrderPayEvent((Order) order));
    }

    public void cancelOrder(OrderLike order) {
        if (order.isNull()) {
            return;
        }
        eventPublisher.publishEvent(new OrderCancelEvent((Order) order));
    }

    public void deliverOrder(OrderLike order) {
        if (order.isNull()) {
            return;
        }
        eventPublisher.publishEvent(new OrderDeliverEvent((Order) order));
    }
}
