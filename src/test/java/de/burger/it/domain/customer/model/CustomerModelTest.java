package de.burger.it.domain.customer.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerModelTest {

    @Test
    void customerDefault_greenPath_validData() {
        UUID id = UUID.randomUUID();
        CustomerDefault c = new CustomerDefault(id, "John Doe", "john@example.com");
        assertThat(c.id(), is(id));
        assertThat(c.name(), is("John Doe"));
        assertThat(c.email(), is("john@example.com"));
    }

    @Test
    void customerDefault_redPath_nullId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(null, "Name", "email@example.com"));
        assertThat(ex.getMessage(), containsString("id cannot be null"));
    }

    @Test
    void customerDefault_redPath_blankName() {
        UUID id = UUID.randomUUID();
        // blank and null name
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(id, "", "email@example.com"));
        assertThat(ex1.getMessage(), containsString("name cannot be null or blank"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(id, "   ", "email@example.com"));
        assertThat(ex2.getMessage(), containsString("name cannot be null or blank"));

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(id, null, "email@example.com"));
        assertThat(ex3.getMessage(), containsString("name cannot be null or blank"));
    }

    @Test
    void customerDefault_redPath_blankEmail() {
        UUID id = UUID.randomUUID();
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(id, "Name", ""));
        assertThat(ex1.getMessage(), containsString("email cannot be null or blank"));

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(id, "Name", "   "));
        assertThat(ex2.getMessage(), containsString("email cannot be null or blank"));

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class,
                () -> new CustomerDefault(id, "Name", null));
        assertThat(ex3.getMessage(), containsString("email cannot be null or blank"));
    }

    @Test
    void customerNullObject_greenPath_singletonAndDefaults() {
        Customer nullCustomer1 = CustomerNullObject.getInstance();
        Customer nullCustomer2 = CustomerNullObject.getInstance();

        // Singleton
        assertThat(nullCustomer1, sameInstance(nullCustomer2));

        // Default values
        assertThat(nullCustomer1.id().toString(), is("00000000-0000-0000-0000-000000000000"));
        assertThat(nullCustomer1.name(), is(""));
        assertThat(nullCustomer1.email(), is(""));
        assertThat(nullCustomer1.isNull(), is(true));
    }
}
