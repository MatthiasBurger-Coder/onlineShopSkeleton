package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSuspendEventTest {

    @Test
    void constructor_shouldHoldCustomer_GreenPath() {
        // Given
        Customer customer = new CustomerDefault(UUID.randomUUID(), "Carol", "carol@example.com");

        // When
        CustomerSuspendEvent event = new CustomerSuspendEvent(customer);

        // Then
        assertSame(customer, event.customer(), "Event should return the same customer instance");
        assertFalse(event.customer().isNull(), "Customer should not be a NullObject");
    }

    @Test
    void constructor_whenNull_shouldAllowNull_RedPath() {
        // When
        CustomerSuspendEvent event = new CustomerSuspendEvent(null);

        // Then
        assertNull(event.customer(), "Customer may be null in event");
    }
}
