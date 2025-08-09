package de.burger.it.domain.cart.port;

import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.state.CartStateType;

import java.util.UUID;


public interface CartStatusAssignmentPort {

    CartStateType findBy(UUID cartId);

    void assign(CartDefault cart, CartStateType newState);
}
