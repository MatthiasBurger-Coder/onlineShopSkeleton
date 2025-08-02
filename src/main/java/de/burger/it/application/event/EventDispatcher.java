package de.burger.it.application.event;

import de.burger.it.domain.common.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * A centralized event dispatcher that handles publishing events and routing them to processors.
 * This class decouples event publishing from event processing.
 */
@Component
public class EventDispatcher {
    
    private final ApplicationEventPublisher publisher;
    
    public EventDispatcher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    /**
     * Dispatches an event to the appropriate event processor.
     * This method uses Spring's ApplicationEventPublisher to publish the event,
     * which will be picked up by the appropriate event listener.
     * 
     * @param event the event to dispatch
     * @param <E> the type of the event
     */
    public <E extends DomainEvent> void dispatch(E event) {
        if (event == null) {
            return;
        }
        publisher.publishEvent(event);
    }
}