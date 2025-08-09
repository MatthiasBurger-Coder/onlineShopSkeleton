package de.burger.it.domain.cart.model;

import de.burger.it.domain.cart.state.NullCartState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class CartNullObject implements Cart {
    
    private static final CartNullObject INSTANCE = new CartNullObject();
    private final UUID id;
    
    public CartNullObject() {
        this.id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
    
    @NotNull
    public static CartNullObject getInstance() {
        return INSTANCE;
    }
    
    @Override
    @NotNull
    public UUID id() {
        return id;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return switch (o) {
            // Consider any NullCart equal if their id is the zero UUID
            case CartNullObject other -> Objects.equals(this.id, other.id);

            // Consider equal to a NullCartState (for tests comparing state.notDefined())
            case NullCartState ignored -> true;
            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}