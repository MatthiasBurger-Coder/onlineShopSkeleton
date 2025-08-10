package de.burger.it.domain.customer.model;

import de.burger.it.domain.customer.state.CustomerStateType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerStatusAssignmentTest {

    @Test
    void record_shouldHoldValues_GreenPath() {
        UUID id = UUID.randomUUID();
        CustomerStatusAssignment assignment = new CustomerStatusAssignment(id, CustomerStateType.SUSPENDED);

        assertEquals(id, assignment.customerId());
        assertEquals(CustomerStateType.SUSPENDED, assignment.state());
    }

    @Test
    void record_whenNulls_shouldAllowNulls_RedPath() {
        CustomerStatusAssignment assignment = new CustomerStatusAssignment(null, null);

        assertNull(assignment.customerId());
        assertNull(assignment.state());
    }
}
