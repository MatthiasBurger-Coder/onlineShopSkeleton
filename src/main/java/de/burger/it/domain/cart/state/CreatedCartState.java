package de.burger.it.domain.cart.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the created state of a cart.
 * This is the initial state when a cart is first created.
 */
@ToString
@NoArgsConstructor
public class CreatedCartState implements CartState {
    
    @Override
    public CartState create() {
        return this;
    }

    @Override
    public CartState activate() {
        return new ActiveCartState();
    }

    @Override
    public CartState order() {
        throw new IllegalStateException("Cart must be active before ordering");
    }

    @Override
    public CartState close() {
        return new ClosedCartState();
    }

    @Override
    public CartStateType code() {
        return CartStateType.CREATED;
    }

    @Override
    public CartState notDefined() {
        throw new IllegalStateException("Cart is null");
    }
}
