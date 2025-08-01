package de.burger.it.domain.order.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record NullOrder(@NotNull UUID id) implements OrderLike {
    
    private static final NullOrder INSTANCE = new NullOrder(
            UUID.fromString("00000000-0000-0000-0000-000000000000"));
    
    @NotNull
    public static NullOrder getInstance() {
        return INSTANCE;
    }
    
    @Override
    public boolean isNull() {
        return true;
    }
    
    @Override
    @NotNull
    public String toString() {
        return "NullOrder";
    }
}