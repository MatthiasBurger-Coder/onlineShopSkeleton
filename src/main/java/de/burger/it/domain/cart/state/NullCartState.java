package de.burger.it.domain.cart.state;

import de.burger.it.domain.cart.model.CartNullObject;
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

    @Override
    public boolean equals(Object obj) {
        return switch (obj) {
            case NullCartState ignored -> true;
            case CartNullObject ignored -> true;
            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        // constant hash code as all NullCartState instances are considered equal
        return 0;
    }
}
