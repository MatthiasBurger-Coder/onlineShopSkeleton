package de.burger.it.domain.customer.state;

import lombok.ToString;

@ToString
public class SuspendedState implements CustomerState {
    @Override
    public CustomerState create() {
        throw new IllegalStateException("Cannot create customer in suspended state");
    }

    public CustomerState suspend() {
        return this;
    }

    public CustomerState active() {
        return new ActivetedState();
    }

    public CustomerStateType code() {
        return CustomerStateType.SUSPENDED;
    }
}
