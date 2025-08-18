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

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Order createNewOrder(Cart cart) {
        return Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .map(c -> {
                    var order = new OrderDefault(UUID.randomUUID());
                    publisher.publishEvent(new OrderCreateEvent(order));
                    return (Order) order;
                })
                .orElseGet(OrderNullObject::getInstance);
    }

    public void payOrder(Order order) {
        Optional.ofNullable(order)
                .filter(o -> !o.isNull())
                .ifPresent(o -> publisher.publishEvent(new OrderPayEvent(o)));
    }

    public void cancelOrder(Order order) {
        Optional.ofNullable(order)
                .filter(o -> !o.isNull())
                .ifPresent(o -> publisher.publishEvent(new OrderCancelEvent(o)));
    }

    public void deliverOrder(Order order) {
        Optional.ofNullable(order)
                .filter(o -> !o.isNull())
                .ifPresent(o -> publisher.publishEvent(new OrderDeliverEvent(o)));
    }
}
