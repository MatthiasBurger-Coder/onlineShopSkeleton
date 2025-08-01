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
     * Checks if this is a null customer.
     */
    boolean isNull();
}