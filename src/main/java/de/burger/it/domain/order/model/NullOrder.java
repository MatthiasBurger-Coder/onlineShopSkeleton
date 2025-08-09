package de.burger.it.domain.order.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class NullOrder implements OrderLike {
    
    private static final NullOrder INSTANCE = new NullOrder();
    private final UUID id;
    
    private NullOrder() {
        this.id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
    
    @NotNull
    public static NullOrder getInstance() {
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
}