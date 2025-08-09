package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;

/**
 * Event emitted when an order is paid.
 */
public record OrderPayEvent(Order order) {

}