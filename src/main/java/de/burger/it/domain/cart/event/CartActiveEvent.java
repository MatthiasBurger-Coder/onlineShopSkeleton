package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;

/**
 * Event fired when a cart becomes active.
 */
public record CartActiveEvent(Cart cart, Customer customer) {

}
