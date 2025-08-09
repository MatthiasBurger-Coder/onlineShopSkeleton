package de.burger.it.domain.customer.model;

import java.util.UUID;

/**
 * Common interface for Customer and NullCustomer.
 */
public interface CustomerLike {
    /**
     * Returns the customer ID.
     */
    UUID id();
    
    /**
     * Returns the customer name.
     */
    String name();
    
    /**
     * Returns the customer email.
     */
    String email();

    /**
     * Indicates whether this is a Null Object instance.
     */
    default boolean isNull() {
        return false;
    }
}