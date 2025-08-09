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
     * Represents a transition to a "not defined" or null state.
     * This method is intended for scenarios where the state is undefined
     * or unavailable, resulting in a fallback to a null object state.
     *
     * @return an instance of a null or placeholder cart state
     */
    CartState notDefined();
    
    /**
     * Gets the type code for this state.
     *
     * @return the cart state type
     */
    CartStateType code();
}
