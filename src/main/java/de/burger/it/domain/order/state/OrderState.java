package de.burger.it.domain.order.state;

/**
 * Interface representing the state of an order.
 * Implements the State pattern to manage order state transitions.
 */
public interface OrderState {
    
    /**
     * Transitions to the paid state.
     *
     * @return the new order state
     * @throws IllegalStateException if the transition is not allowed
     */
    OrderState pay();
    
    /**
     * Transitions to the canceled state.
     *
     * @return the new order state
     * @throws IllegalStateException if the transition is not allowed
     */
    OrderState cancel();
    
    /**
     * Transitions to the delivered state.
     *
     * @return the new order state
     * @throws IllegalStateException if the transition is not allowed
     */
    OrderState deliver();
    
    /**
     * Gets the type code for this state.
     *
     * @return the order state type
     */
    OrderStateType code();
}
