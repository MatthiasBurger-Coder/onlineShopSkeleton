package de.burger.it.application.event;

/**
 * Generic interface for event handlers.
 * This interface standardizes the way events are handled across the application.
 * 
 * @param <E> the type of event this handler can process
 */
@FunctionalInterface
public interface EventHandler<E> {
    
    /**
     * Handles the given event.
     * 
     * @param event the event to handle
     * @return the processed event, which may be modified during handling
     */
    E handle(E event);
    
    /**
     * Composes this handler with another handler, creating a new handler that
     * applies this handler first, then the other handler.
     * 
     * @param after the handler to apply after this handler
     * @return a new handler that applies this handler first, then the other handler
     */
    default EventHandler<E> andThen(EventHandler<E> after) {
        return event -> after.handle(this.handle(event));
    }
}