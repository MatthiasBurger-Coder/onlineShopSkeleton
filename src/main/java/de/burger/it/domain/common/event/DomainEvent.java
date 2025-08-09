package de.burger.it.domain.common.event;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events.
 * Provides common properties and behavior for domain events.
 */
@Getter
public abstract class DomainEvent {
    
    private final UUID eventId;
    private final Instant timestamp;
    
    /**
     * Creates a new domain event with a random ID and the current timestamp.
     */
    protected DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.timestamp = Instant.now();
    }
}