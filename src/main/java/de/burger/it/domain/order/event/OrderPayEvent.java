package de.burger.it.domain.order.event;

import de.burger.it.domain.order.model.Order;

public record OrderPayEvent(Order order) { }