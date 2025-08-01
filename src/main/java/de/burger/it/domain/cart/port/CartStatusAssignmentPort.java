package de.burger.it.domain.cart.port;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.state.CartStateType;

import java.util.UUID;


public interface CartStatusAssignmentPort {

    CartStateType findBy(UUID cartId);

    void assign(Cart cart, CartStateType newState);
}
