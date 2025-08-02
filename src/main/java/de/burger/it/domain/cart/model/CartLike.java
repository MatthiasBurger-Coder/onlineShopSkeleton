package de.burger.it.domain.cart.model;

import de.burger.it.domain.common.model.Nullable;
import java.util.UUID;

/**
 * Common interface for Cart and NullCart.
 */
public interface CartLike extends Nullable {
    /**
     * Returns the cart ID.
     */
    UUID id();
}