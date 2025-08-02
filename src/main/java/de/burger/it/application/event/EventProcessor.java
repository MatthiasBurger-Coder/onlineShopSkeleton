package de.burger.it.application.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A generic event processor that manages event handlers for different event types.
 * This class provides a centralized way to register and execute event handlers.
 * 
 * @param <E> the base type of events this processor can handle
 */
public class EventProcessor<E> {
    
    private final Map<Class<?>, List<EventHandler<? extends E>>> handlers = new HashMap<>();
    
    /**
     * Registers a handler for a specific event type.
     * 
     * @param <T> the specific event type
     * @param eventType the class of the event type
     * @param handler the handler to register
     * @return this processor for method chaining
     */
    public <T extends E> EventProcessor<E> register(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        return this;
    }
    
    /**
     * Processes an event by executing all registered handlers for its type.
     * 
     * @param <T> the specific event type
     * @param event the event to process
     * @return the processed event
     */
    @SuppressWarnings("unchecked")
    public <T extends E> T process(T event) {
        if (event == null) {
            return null;
        }
        
        List<EventHandler<? extends E>> eventHandlers = handlers.getOrDefault(event.getClass(), List.of());
        T result = event;
        
        for (EventHandler<? extends E> handler : eventHandlers) {
            result = ((EventHandler<T>) handler).handle(result);
        }
        
        return result;
    }
}