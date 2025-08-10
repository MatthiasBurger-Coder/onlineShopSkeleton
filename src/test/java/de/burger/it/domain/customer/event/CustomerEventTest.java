package de.burger.it.domain.customer.event;

import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CustomerEventTest {

    @Test
    void customerCreateEvent_greenPath_holdsCustomer() {
        Customer customer = new CustomerDefault(UUID.randomUUID(), "Jane", "jane@example.com");
        CustomerCreateEvent event = new CustomerCreateEvent(customer);
        assertThat(event.customer(), sameInstance(customer));
    }

    @Test
    void customerSuspendEvent_greenPath_holdsCustomer() {
        Customer customer = new CustomerDefault(UUID.randomUUID(), "Max", "max@example.com");
        CustomerSuspendEvent event = new CustomerSuspendEvent(customer);
        assertThat(event.customer(), sameInstance(customer));
    }
}
