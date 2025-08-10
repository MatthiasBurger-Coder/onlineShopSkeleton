package de.burger.it.application.customer.process;

import de.burger.it.application.customer.handler.OnCustomerCreateAssignActive;
import de.burger.it.application.customer.handler.OnCustomerCreateNewCart;
import de.burger.it.application.customer.handler.OnCustomerCreateSaveRepository;
import de.burger.it.application.customer.handler.OnCustomerSuspendAssignSuspend;
import de.burger.it.application.customer.handler.OnCustomerSuspendSaveRepository;
import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.model.CustomerDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerProcessConfigTest {

    @Mock
    private OnCustomerCreateAssignActive onCustomerCreateAssignActive;
    @Mock
    private OnCustomerCreateSaveRepository onCustomerCreateSaveRepository;
    @Mock
    private OnCustomerCreateNewCart onCustomerCreateNewCart;

    @Mock
    private OnCustomerSuspendAssignSuspend onCustomerSuspendAssignSuspend;
    @Mock
    private OnCustomerSuspendSaveRepository onCustomerSuspendSaveRepository;

    private CustomerProcessConfig config;

    @BeforeEach
    void setUp() {
        config = new CustomerProcessConfig();
    }

    @Test
    void customerCreateProcessPipeline_shouldExecuteHandlersInOrder_GreenPath() {
        // Given
        ProcessPipeline<CustomerCreateEvent> pipeline = config.customerCreateProcessPipeline(
                onCustomerCreateAssignActive,
                onCustomerCreateSaveRepository,
                onCustomerCreateNewCart
        );
        var event = new CustomerCreateEvent(new CustomerDefault(UUID.randomUUID(), "Max Mustermann", "max@example.com"));

        // When
        CustomerCreateEvent result = pipeline.execute(event);

        // Then
        InOrder inOrder = Mockito.inOrder(onCustomerCreateAssignActive, onCustomerCreateSaveRepository, onCustomerCreateNewCart);
        inOrder.verify(onCustomerCreateAssignActive).execute(event);
        inOrder.verify(onCustomerCreateSaveRepository).execute(event);
        inOrder.verify(onCustomerCreateNewCart).execute(event);
        // Pipeline should return the same event instance (consumer steps don't transform)
        assertSame(event, result);
    }

    @Test
    void customerCreateProcessPipeline_whenFirstHandlerThrows_shouldShortCircuit_RedPath() {
        // Given
        ProcessPipeline<CustomerCreateEvent> pipeline = config.customerCreateProcessPipeline(
                onCustomerCreateAssignActive,
                onCustomerCreateSaveRepository,
                onCustomerCreateNewCart
        );
        var event = new CustomerCreateEvent(new CustomerDefault(UUID.randomUUID(), "Erika Musterfrau", "erika@example.com"));

        doThrow(new IllegalStateException("boom")).when(onCustomerCreateAssignActive).execute(event);

        // When / Then
        assertThrows(IllegalStateException.class, () -> pipeline.execute(event));
        verify(onCustomerCreateAssignActive).execute(event);
        verifyNoInteractions(onCustomerCreateSaveRepository);
        verifyNoInteractions(onCustomerCreateNewCart);
    }

    @Test
    void customerCreateProcessPipeline_whenSecondHandlerThrows_shouldNotCallThird_RedPath() {
        // Given
        ProcessPipeline<CustomerCreateEvent> pipeline = config.customerCreateProcessPipeline(
                onCustomerCreateAssignActive,
                onCustomerCreateSaveRepository,
                onCustomerCreateNewCart
        );
        var event = new CustomerCreateEvent(new CustomerDefault(UUID.randomUUID(), "Erik Example", "erik@example.com"));

        // First succeeds, second throws
        doNothing().when(onCustomerCreateAssignActive).execute(event);
        doThrow(new RuntimeException("fail at save")).when(onCustomerCreateSaveRepository).execute(event);

        // When / Then
        assertThrows(RuntimeException.class, () -> pipeline.execute(event));
        verify(onCustomerCreateAssignActive).execute(event);
        verify(onCustomerCreateSaveRepository).execute(event);
        verifyNoInteractions(onCustomerCreateNewCart);
    }

    @Test
    void customerSuspendProcessPipeline_shouldExecuteHandlersInOrder_GreenPath() {
        // Given
        ProcessPipeline<CustomerSuspendEvent> pipeline = config.customerSuspendProcessPipeline(
                onCustomerSuspendAssignSuspend,
                onCustomerSuspendSaveRepository
        );
        var event = new CustomerSuspendEvent(new CustomerDefault(UUID.randomUUID(), "John Doe", "john@example.com"));

        // When
        CustomerSuspendEvent result = pipeline.execute(event);

        // Then
        InOrder inOrder = Mockito.inOrder(onCustomerSuspendAssignSuspend, onCustomerSuspendSaveRepository);
        inOrder.verify(onCustomerSuspendAssignSuspend).execute(event);
        inOrder.verify(onCustomerSuspendSaveRepository).execute(event);
        assertSame(event, result);
    }

    @Test
    void customerSuspendProcessPipeline_whenFirstHandlerThrows_shouldShortCircuit_RedPath() {
        // Given
        ProcessPipeline<CustomerSuspendEvent> pipeline = config.customerSuspendProcessPipeline(
                onCustomerSuspendAssignSuspend,
                onCustomerSuspendSaveRepository
        );
        var event = new CustomerSuspendEvent(new CustomerDefault(UUID.randomUUID(), "Jane Doe", "jane@example.com"));

        doThrow(new IllegalArgumentException("invalid state")).when(onCustomerSuspendAssignSuspend).execute(event);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> pipeline.execute(event));
        verify(onCustomerSuspendAssignSuspend).execute(event);
        verifyNoInteractions(onCustomerSuspendSaveRepository);
    }

    @Test
    void customerSuspendProcessPipeline_whenSecondHandlerThrows_shouldNotContinue_RedPath() {
        // Given
        ProcessPipeline<CustomerSuspendEvent> pipeline = config.customerSuspendProcessPipeline(
                onCustomerSuspendAssignSuspend,
                onCustomerSuspendSaveRepository
        );
        var event = new CustomerSuspendEvent(new CustomerDefault(UUID.randomUUID(), "Jake Doe", "jake@example.com"));

        doNothing().when(onCustomerSuspendAssignSuspend).execute(event);
        doThrow(new RuntimeException("save failed")).when(onCustomerSuspendSaveRepository).execute(event);

        // When / Then
        assertThrows(RuntimeException.class, () -> pipeline.execute(event));
        verify(onCustomerSuspendAssignSuspend).execute(event);
        verify(onCustomerSuspendSaveRepository).execute(event);
    }
}
