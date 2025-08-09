package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.Customer;

/**
 * Event fired when a cart becomes active.
 */
public record CartActiveEvent(CartDefault cart, Customer customer) {

}
