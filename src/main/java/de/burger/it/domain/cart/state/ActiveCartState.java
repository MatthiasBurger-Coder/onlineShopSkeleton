package de.burger.it.domain.cart.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the active state of a cart.
 * This state indicates that the cart is active and can be used for ordering.
 */
@ToString
@NoArgsConstructor
public class ActiveCartState implements CartState {
    
    @Override
    public CartState create() {
        throw new IllegalStateException("Cart is already created");
    }

    @Override
    public CartState activate() {
        return this;
    }

    @Override
    public CartState order() {
        return new OrderedCartState();
    }

    @Override
    public CartState close() {
        return new ClosedCartState();
    }

    @Override
    public CartStateType code() {
        return CartStateType.ACTIVE;
    }
}
