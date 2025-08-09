package de.burger.it.infrastructure.customer.adapter;

import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerStatusAssignmentAdapterTest {

    private CustomerStatusAssignmentAdapter adapter;
    private CustomerDefault customer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        adapter = new CustomerStatusAssignmentAdapter();
        customerId = UUID.randomUUID();
        customer = new CustomerDefault(customerId, "Test Customer", "test@example.com");
    }

    // GreenPath Tests

    @Test
    void assign_shouldStoreAssignment() {
        // When
        adapter.assign(customer, CustomerStateType.CREATE);
        
        // Then
        CustomerStateType state = adapter.findBy(customerId);
        assertEquals(CustomerStateType.CREATE, state);
    }

    @Test
    void assign_shouldUpdateExistingAssignment() {
        // Given
        adapter.assign(customer, CustomerStateType.CREATE);
        
        // When
        adapter.assign(customer, CustomerStateType.ACTIVE);
        
        // Then
        CustomerStateType state = adapter.findBy(customerId);
        assertEquals(CustomerStateType.ACTIVE, state);
    }

    @Test
    void findBy_shouldReturnCorrectState() {
        // Given
        adapter.assign(customer, CustomerStateType.SUSPENDED);
        
        // When
        CustomerStateType state = adapter.findBy(customerId);
        
        // Then
        assertEquals(CustomerStateType.SUSPENDED, state);
    }

    // RedPath Tests

    @Test
    void assign_whenCustomerIsNull_shouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(null, CustomerStateType.CREATE));
    }

    @Test
    void assign_whenStateIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(IllegalArgumentException.class, () -> adapter.assign(customer, null));
    }

    @Test
    void findBy_whenCustomerIdIsNull_shouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> adapter.findBy(null));
    }

    @Test
    void findBy_whenCustomerIdNotFound_shouldReturnNull() {
        // When
        CustomerStateType state = adapter.findBy(UUID.randomUUID());

        // Then
        assertNull(state);
    }
}