package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.common.event.DomainEvent;
import de.burger.it.domain.customer.model.Customer;
import lombok.Getter;

/**
 * Event fired when a cart is closed.
 */
@Getter
public class CartCloseEvent extends DomainEvent {

    private final CartDefault cart;
    private final Customer customer;

    public CartCloseEvent(CartDefault cart, Customer customer) {
        this.cart = cart;
        this.customer = customer;
    }

}
