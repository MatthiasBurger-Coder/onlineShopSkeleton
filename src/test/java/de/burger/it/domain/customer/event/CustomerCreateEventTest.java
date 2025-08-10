package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerCreateEventTest {

    @Test
    void constructor_shouldHoldCustomer_GreenPath() {
        Customer customer = new CustomerDefault(UUID.randomUUID(), "Bob", "bob@example.com");
        CustomerCreateEvent event = new CustomerCreateEvent(customer);
        assertSame(customer, event.customer());
        assertFalse(event.customer().isNull());
    }

    @Test
    void constructor_whenNull_shouldAllowNull_RedPath() {
        CustomerCreateEvent event = new CustomerCreateEvent(null);
        assertNull(event.customer());
    }
}
