package de.burger.it.application.order.handler;

import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.port.OrderRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class OnOrderCreateSaveRepositoryTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    private OnOrderCreateSaveRepository handler;

    @BeforeEach
    void setUp() {
        handler = new OnOrderCreateSaveRepository(orderRepositoryPort);
    }

    // Greenpath: Should save order into repository when a valid order create event is executed
    @Test
    void execute_shouldSaveOrderInRepository() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderCreateEvent(order);

        handler.execute(event);

        verify(orderRepositoryPort).save(order);
        verifyNoMoreInteractions(orderRepositoryPort);
    }

    // Redpath: If repository throws, the exception should propagate
    @Test
    void execute_whenRepositoryThrows_shouldPropagateException() {
        var order = new OrderDefault(UUID.randomUUID());
        var event = new OrderCreateEvent(order);

        doThrow(new RuntimeException("save failed"))
                .when(orderRepositoryPort)
                .save(order);

        assertThrows(RuntimeException.class, () -> handler.execute(event));
        verify(orderRepositoryPort).save(order);
    }
}
