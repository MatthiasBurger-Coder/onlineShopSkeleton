package de.burger.it.domain.customer.model;

import java.util.UUID;

public final class CustomerNullObject implements Customer {

    private static final CustomerNullObject INSTANCE = new CustomerNullObject();
    private final UUID id;
    private final String name;
    private final String email;

    private CustomerNullObject() {
        this.id = UUID.fromString("00000000-0000-0000-0000-000000000000");
        this.name = "";
        this.email = "";
    }

    public static CustomerNullObject getInstance() {
        return INSTANCE;
    }
    
    @Override
    public UUID id() {
        return id;
    }
    
    @Override
    public String name() {
        return name;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public boolean isNull() {
        return true;
    }
}