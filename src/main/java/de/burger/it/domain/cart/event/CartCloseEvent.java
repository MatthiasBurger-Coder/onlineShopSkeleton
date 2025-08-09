package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;

/**
 * Event fired when a cart is closed.
 */
public record CartCloseEvent(Cart cart, Customer customer) {

}
