package de.burger.it.domain.customer.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the suspended state of a customer.
 * This state indicates that the customer account has been temporarily suspended.
 */
@ToString
@NoArgsConstructor
public class SuspendedState implements CustomerState {
    
    @Override
    public CustomerState create() {
        throw new IllegalStateException("Cannot create customer in suspended state");
    }

    @Override
    public CustomerState suspend() {
        return this;
    }

    @Override
    public CustomerState active() {
        return new ActivatedState();
    }

    @Override
    public CustomerStateType code() {
        return CustomerStateType.SUSPENDED;
    }
}
