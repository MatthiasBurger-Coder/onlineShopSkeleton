package de.burger.it.application.customer.listener;

import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerEventListenerTest {

    @Mock
    private ProcessPipeline<CustomerCreateEvent> customerCreatePipeline;
    @Mock
    private ProcessPipeline<CustomerSuspendEvent> customerSuspendPipeline;

    private CustomerEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new CustomerEventListener(customerCreatePipeline, customerSuspendPipeline);
    }

    // Green path tests
    @Test
    void onCustomerCreate_shouldDelegateToCreatePipeline_GreenPath() {
        var customer = new CustomerDefault(UUID.randomUUID(), "John Doe", "john@example.com");
        var event = new CustomerCreateEvent(customer);

        listener.onCustomerCreate(event);

        verify(customerCreatePipeline).execute(event);
        verifyNoInteractions(customerSuspendPipeline);
    }

    @Test
    void onCustomerSuspend_shouldDelegateToSuspendPipeline_GreenPath() {
        var customer = new CustomerDefault(UUID.randomUUID(), "Jane Doe", "jane@example.com");
        var event = new CustomerSuspendEvent(customer);

        listener.onCustomerSuspend(event);

        verify(customerSuspendPipeline).execute(event);
        verifyNoInteractions(customerCreatePipeline);
    }

    // Red path tests
    @Test
    void onCustomerCreate_whenPipelineThrows_shouldPropagate_RedPath() {
        var customer = new CustomerDefault(UUID.randomUUID(), "Error User", "error1@example.com");
        var event = new CustomerCreateEvent(customer);

        doThrow(new RuntimeException("create failed")).when(customerCreatePipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.onCustomerCreate(event));
        verify(customerCreatePipeline).execute(event);
        verifyNoInteractions(customerSuspendPipeline);
    }

    @Test
    void onCustomerSuspend_whenPipelineThrows_shouldPropagate_RedPath() {
        var customer = new CustomerDefault(UUID.randomUUID(), "Error User2", "error2@example.com");
        var event = new CustomerSuspendEvent(customer);

        doThrow(new RuntimeException("suspend failed")).when(customerSuspendPipeline).execute(event);

        assertThrows(RuntimeException.class, () -> listener.onCustomerSuspend(event));
        verify(customerSuspendPipeline).execute(event);
        verifyNoInteractions(customerCreatePipeline);
    }
}
