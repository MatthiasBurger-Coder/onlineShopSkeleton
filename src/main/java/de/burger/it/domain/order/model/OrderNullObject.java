package de.burger.it.domain.order.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class OrderNullObject implements OrderLike {
    
    private static final OrderNullObject INSTANCE = new OrderNullObject();
    private final UUID id;
    
    private OrderNullObject() {
        this.id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
    
    @NotNull
    public static OrderNullObject getInstance() {
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