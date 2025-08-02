package de.burger.it.domain.cart.state;

/**
 * Null object implementation for {@link CartState}.
 * Used when no real cart state is available.
 */
public final class NullCartState implements CartState {

    private static final NullCartState INSTANCE = new NullCartState();

    private NullCartState() {
    }

    public static NullCartState getInstance() {
        return INSTANCE;
    }

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
}
