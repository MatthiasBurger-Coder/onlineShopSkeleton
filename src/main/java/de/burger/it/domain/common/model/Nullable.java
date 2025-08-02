package de.burger.it.domain.common.model;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Common interface for all domain objects that can have a null representation.
 * This interface provides methods to handle null objects in a functional way,
 * similar to Optional but specifically designed for domain objects.
 */
public interface Nullable {
    /**
     * Checks if this is a null object.
     * 
     * @return true if this is a null object, false otherwise
     */
    boolean isNull();
    
    /**
     * Checks if this is a non-null object.
     * 
     * @return true if this is a non-null object, false otherwise
     */
    default boolean isPresent() {
        return !isNull();
    }
    
    /**
     * If this object is non-null, performs the given action with it.
     * 
     * @param action the action to perform with this object
     */
    default void ifPresent(Consumer<? super Nullable> action) {
        if (isPresent()) {
            action.accept(this);
        }
    }
    
    /**
     * If this object is non-null, applies the given mapping function to it,
     * otherwise returns the result of the supplier.
     * 
     * @param <R> the type of the result
     * @param mapper the mapping function to apply
     * @param defaultValue the value to return if this object is null
     * @return the result of applying the mapping function to this object if non-null,
     *         otherwise the default value
     */
    default <R> R map(Function<? super Nullable, ? extends R> mapper, R defaultValue) {
        return isPresent() ? mapper.apply(this) : defaultValue;
    }
    
    /**
     * Converts this object to an Optional.
     * 
     * @return an Optional containing this object if non-null, otherwise an empty Optional
     */
    default Optional<Nullable> toOptional() {
        return isPresent() ? Optional.of(this) : Optional.empty();
    }
}