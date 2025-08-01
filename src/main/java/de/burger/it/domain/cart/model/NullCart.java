package de.burger.it.domain.cart.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record NullCart(@NotNull UUID id) implements CartLike {
    
    private static final NullCart INSTANCE = new NullCart(
            UUID.fromString("00000000-0000-0000-0000-000000000000"));
    
    @NotNull
    public static NullCart getInstance() {
        return INSTANCE;
    }
    
    @Override
    public boolean isNull() {
        return true;
    }
    
    @Override
    @NotNull
    public String toString() {
        return "NullCart";
    }
}