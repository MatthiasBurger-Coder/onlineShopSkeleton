package de.burger.it.domain.order.model;

import java.util.UUID;

/**
 * Common interface for Order and NullOrder.
 */
public interface OrderLike {
    /**
     * Returns the order ID.
     */
    UUID id();
    
    /**
     * Checks if this is a null order.
     */
    boolean isNull();
}