package de.burger.it.domain.cart.model;

import java.util.UUID;

/**
 * Common interface for Cart and NullCart.
 */
public interface CartLike {
    /**
     * Returns the cart ID.
     */
    UUID id();
    
    /**
     * Checks if this is a null cart.
     */
    boolean isNull();
}