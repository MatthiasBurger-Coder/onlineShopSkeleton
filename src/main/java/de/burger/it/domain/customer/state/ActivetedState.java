package de.burger.it.domain.customer.state;

import lombok.ToString;

@ToString
public class ActivetedState implements CustomerState {
    @Override
    public CustomerState create() {
        throw new IllegalStateException("Cannot create customer in active state");
    }

    public CustomerState suspend() {
        return new SuspendedState();
    }

    public CustomerState active() {
        return this;
    }

    public CustomerStateType code() {
        return CustomerStateType.ACTIVE;
    }
}
