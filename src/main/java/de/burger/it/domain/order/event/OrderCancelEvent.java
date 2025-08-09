package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event emitted when an order is cancelled.
 */
@Getter
public class OrderCancelEvent extends DomainEvent {

    private final Order order;

    public OrderCancelEvent(Order order) {
        this.order = order;
    }

}