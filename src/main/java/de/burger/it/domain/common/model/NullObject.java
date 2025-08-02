package de.burger.it.domain.common.model;

import org.jetbrains.annotations.NotNull;

/**
 * Base class for all null object implementations.
 * Provides common functionality for null objects.
 * 
 * @param <T> the type of the null object
 */
public abstract class NullObject<T extends Nullable> implements Nullable {
    
    @Override
    public boolean isNull() {
        return true;
    }
    
    /**
     * Returns a string representation of this null object.
     * 
     * @return a string representation of this null object
     */
    @Override
    @NotNull
    public String toString() {
        return getClass().getSimpleName();
    }
}