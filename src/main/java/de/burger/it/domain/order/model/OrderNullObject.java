package de.burger.it.domain.order.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class OrderNullObject implements Order {
    
    private static final Order INSTANCE = new OrderNullObject();
    private final UUID id;
    
    private OrderNullObject() {
        this.id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
    
    @NotNull
    public static Order getInstance() {
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