package de.burger.it.domain.order.model;

import de.burger.it.domain.common.model.Nullable;
import java.util.UUID;

/**
 * Common interface for Order and NullOrder.
 */
public interface OrderLike extends Nullable {
    /**
     * Returns the order ID.
     */
    UUID id();
}