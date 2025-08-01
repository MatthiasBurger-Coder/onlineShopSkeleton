package de.burger.it.domain.customer.state;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the created state of a customer.
 * This is the initial state when a customer is first created in the system.
 */
@ToString
@NoArgsConstructor
public class CreatedState implements CustomerState {
    
    @Override
    public CustomerState create() {
        return this;
    }

    @Override
    public CustomerState suspend() {
        throw new IllegalStateException("Cannot suspend created customer");
    }

    @Override
    public CustomerState active() {
        return new ActivatedState();
    }

    @Override
    public CustomerStateType code() {
        return CustomerStateType.CREATE;
    }
}
