package de.burger.it.domain.cart.state;

/**
 * Interface representing the state of a cart.
 * Implements the State pattern to manage cart state transitions.
 */
public interface CartState {
    
    /**
     * Transitions to the created state.
     *
     * @return the new cart state
     * @throws IllegalStateException if the transition is not allowed
     */
    CartState create();
    
    /**
     * Transitions to the active state.
     *
     * @return the new cart state
     * @throws IllegalStateException if the transition is not allowed
     */
    CartState activate();
    
    /**
     * Transitions to the ordered state.
     *
     * @return the new cart state
     * @throws IllegalStateException if the transition is not allowed
     */
    CartState order();
    
    /**
     * Transitions to the closed state.
     *
     * @return the new cart state
     * @throws IllegalStateException if the transition is not allowed
     */
    CartState close();
    
    /**
     * Gets the type code for this state.
     *
     * @return the cart state type
     */
    CartStateType code();
}
