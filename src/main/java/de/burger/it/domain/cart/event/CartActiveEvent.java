package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.common.event.DomainEvent;

/**
 * Event fired when a cart becomes active.
 */
public class CartActiveEvent extends DomainEvent {

    private final CartDefault cart;
    private final Customer customer;

    public CartActiveEvent(CartDefault cart, Customer customer) {
        this.cart = cart;
        this.customer = customer;
    }

    public CartDefault getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }
}
