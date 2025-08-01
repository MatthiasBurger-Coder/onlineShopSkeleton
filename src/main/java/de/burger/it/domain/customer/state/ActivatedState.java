package de.burger.it.domain.customer.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the activated state of a customer.
 * This state indicates that the customer account is active and operational.
 */
@ToString
@NoArgsConstructor
public class ActivatedState implements CustomerState {
    
    @Override
    public CustomerState create() {
        throw new IllegalStateException("Cannot create customer in active state");
    }

    @Override
    public CustomerState suspend() {
        return new SuspendedState();
    }

    @Override
    public CustomerState active() {
        return this;
    }

    @Override
    public CustomerStateType code() {
        return CustomerStateType.ACTIVE;
    }
}