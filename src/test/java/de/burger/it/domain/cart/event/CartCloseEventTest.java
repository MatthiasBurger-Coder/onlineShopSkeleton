package de.burger.it.domain.cart.event;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartCloseEventTest {

    @Test
    void constructor_shouldHoldCartAndCustomer_GreenPath() {
        // Given
        Cart cart = new CartDefault(UUID.randomUUID());
        Customer customer = new CustomerDefault(UUID.randomUUID(), "Jane Doe", "jane@example.com");

        // When
        CartCloseEvent event = new CartCloseEvent(cart, customer);

        // Then
        assertSame(cart, event.cart(), "Event should return the same cart instance");
        assertSame(customer, event.customer(), "Event should return the same customer instance");
        assertFalse(event.cart().isNull(), "Cart should not be a NullObject");
        assertFalse(event.customer().isNull(), "Customer should not be a NullObject");
    }

    @Test
    void constructor_whenNullParameters_shouldAllowNulls_RedPath() {
        // When
        CartCloseEvent event = new CartCloseEvent(null, null);

        // Then
        assertNull(event.cart(), "Cart may be null in event");
        assertNull(event.customer(), "Customer may be null in event");
    }
}
