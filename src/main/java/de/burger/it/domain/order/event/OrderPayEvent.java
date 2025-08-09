package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when an order is paid.
 */
@Getter
public class OrderPayEvent extends DomainEvent {

    private final Order order;

    public OrderPayEvent(Order order) {
        this.order = order;
    }

}