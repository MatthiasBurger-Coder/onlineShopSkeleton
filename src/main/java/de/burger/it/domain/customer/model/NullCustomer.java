package de.burger.it.domain.customer.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record NullCustomer(UUID id, String name, String email) implements CustomerLike {
    
    private static final NullCustomer INSTANCE = new NullCustomer(
            UUID.fromString("00000000-0000-0000-0000-000000000000"), "", "");
    
   public static NullCustomer getInstance() {
        return INSTANCE;
    }
    
    @Override
    public boolean isNull() {
        return true;
    }
    
    @Override
    @NotNull
    public String toString() {
        return "NullCustomer";
    }
}