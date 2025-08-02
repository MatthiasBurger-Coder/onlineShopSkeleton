package de.burger.it.domain.customer.state;

/**
 * Null object implementation for {@link CustomerState}.
 */
public final class NullCustomerState implements CustomerState {

    private static final NullCustomerState INSTANCE = new NullCustomerState();

    private NullCustomerState() {
    }

    public static NullCustomerState getInstance() {
        return INSTANCE;
    }

    @Override
    public CustomerState create() {
        return this;
    }

    @Override
    public CustomerState suspend() {
        return this;
    }

    @Override
    public CustomerState active() {
        return this;
    }

    @Override
    public CustomerStateType code() {
        return CustomerStateType.CREATE;
    }
}
