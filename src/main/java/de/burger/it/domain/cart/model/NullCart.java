package de.burger.it.domain.cart.model;

import de.burger.it.domain.common.model.NullObject;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class NullCart extends NullObject<CartLike> implements CartLike {
    
    private static final NullCart INSTANCE = new NullCart();
    private final UUID id;
    
    private NullCart() {
        this.id = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
    
    @NotNull
    public static NullCart getInstance() {
        return INSTANCE;
    }
    
    @Override
    @NotNull
    public UUID id() {
        return id;
    }
}