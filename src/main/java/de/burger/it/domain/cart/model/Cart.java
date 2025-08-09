package de.burger.it.domain.cart.model;

import java.util.UUID;

/**
 * Common interface for Cart and NullCart.
 */
public interface Cart {
    /**
     * Returns the cart ID.
     */
    UUID id();

    /**
     * Indicates whether this is a Null Object instance.
     */
    default boolean isNull() {
        return false;
    }
}