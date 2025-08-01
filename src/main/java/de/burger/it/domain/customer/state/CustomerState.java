package de.burger.it.domain.customer.state;

/**
 * Interface representing the state of a customer.
 * Implements the State pattern to manage customer state transitions.
 */
public interface CustomerState {
    
    /**
     * Transitions to the created state.
     *
     * @return the new customer state
     * @throws IllegalStateException if the transition is not allowed
     */
    CustomerState create();
    
    /**
     * Transitions to the suspended state.
     *
     * @return the new customer state
     * @throws IllegalStateException if the transition is not allowed
     */
    CustomerState suspend();
    
    /**
     * Transitions to the active state.
     *
     * @return the new customer state
     * @throws IllegalStateException if the transition is not allowed
     */
    CustomerState active();
    
    /**
     * Gets the type code for this state.
     *
     * @return the customer state type
     */
    CustomerStateType code();
}
