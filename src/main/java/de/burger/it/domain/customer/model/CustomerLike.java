package de.burger.it.domain.customer.model;

import de.burger.it.domain.common.model.Nullable;
import java.util.UUID;

/**
 * Common interface for Customer and NullCustomer.
 */
public interface CustomerLike extends Nullable {
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
}