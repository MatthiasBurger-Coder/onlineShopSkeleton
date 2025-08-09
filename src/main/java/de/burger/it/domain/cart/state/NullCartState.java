package de.burger.it.domain.cart.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Null object implementation for {@link CartState}.
 * Used when no real cart state is available.
 */
@ToString
@NoArgsConstructor
public final class NullCartState implements CartState {

    @Override
    public CartState create() {
        return this;
    }

    @Override
    public CartState activate() {
        return this;
    }

    @Override
    public CartState order() {
        return this;
    }

    @Override
    public CartState close() {
        return this;
    }

    @Override
    public CartStateType code() {
        return CartStateType.CREATED;
    }

    @Override
    public CartState notDefined()  {
        return this;
    }
}
