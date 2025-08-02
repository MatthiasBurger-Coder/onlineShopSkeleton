package de.burger.it.application.state;

import de.burger.it.application.event.EventHandler;
import de.burger.it.domain.common.event.DomainEvent;

/**
 * A generic handler for state transitions triggered by domain events.
 * This class decouples state transition logic from event handling.
 * 
 * @param <E> the type of event that triggers the state transition
 * @param <S> the type of state that is being transitioned
 */
public abstract class StateTransitionHandler<E extends DomainEvent, S> implements EventHandler<E> {
    
    /**
     * Handles the event by performing a state transition.
     * 
     * @param event the event that triggered the state transition
     * @return the processed event
     */
    @Override
    public E handle(E event) {
        performStateTransition(event);
        return event;
    }
    
    /**
     * Performs the actual state transition based on the event.
     * This method should be implemented by subclasses to define
     * the specific state transition logic.
     * 
     * @param event the event that triggered the state transition
     */
    protected abstract void performStateTransition(E event);
}