package de.burger.it.domain.order.model;

import java.util.UUID;

/**
 * Common interface for OrderDefault and NullOrder.
 */
public interface Order {
    /**
     * Returns the order ID.
     */
    UUID id();

    /**
     * Indicates whether this is a Null Object instance.
     */
    default boolean isNull() {
        return false;
    }
}