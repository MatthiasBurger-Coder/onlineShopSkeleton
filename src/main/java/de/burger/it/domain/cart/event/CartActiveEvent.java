package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.common.event.DomainEvent;

/**
 * Event fired when a cart becomes active.
 */
public class CartActiveEvent extends DomainEvent {

    private final Cart cart;
    private final Customer customer;

    public CartActiveEvent(Cart cart, Customer customer) {
        this.cart = cart;
        this.customer = customer;
    }

    public Cart getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }
}
