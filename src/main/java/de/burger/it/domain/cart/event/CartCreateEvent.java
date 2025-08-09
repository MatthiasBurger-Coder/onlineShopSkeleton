package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;

/**
 * Event fired whenever a new cart is created for a customer.
 */
public record CartCreateEvent(Cart cart, Customer customer) {

}
