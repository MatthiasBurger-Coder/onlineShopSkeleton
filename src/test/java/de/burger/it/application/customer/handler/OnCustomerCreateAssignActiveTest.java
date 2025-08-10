package de.burger.it.application.customer.handler;

import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.model.CustomerDefault;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OnCustomerCreateAssignActiveTest {

    @Mock
    private CustomerStatusAssignmentPort customerStatusAssignmentPort;

    private OnCustomerCreateAssignActive handler;

    @BeforeEach
    void setUp() {
        handler = new OnCustomerCreateAssignActive(customerStatusAssignmentPort);
    }

    @Test
    void execute_shouldAssignActiveState_GreenPath() {
        // Given
        var customer = new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com");
        var event = new CustomerCreateEvent(customer);

        // When
        handler.execute(event);

        // Then
        verify(customerStatusAssignmentPort).assign(customer, CustomerStateType.ACTIVE);
    }

    @Test
    void execute_whenEventIsNull_shouldThrowException_RedPath() {
        // When / Then
        assertThrows(NullPointerException.class, () -> handler.execute(null));
        verifyNoInteractions(customerStatusAssignmentPort);
    }
}
