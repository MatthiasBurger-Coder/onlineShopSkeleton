package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;

public record CartActiveEvent(Cart cart, Customer customer) {
}
