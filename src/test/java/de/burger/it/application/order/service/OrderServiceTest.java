package de.burger.it.application.order.service;

import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.CartNullObject;
import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderNullObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private OrderService orderService;
    private Cart cart;
    private Order order;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(eventPublisher);
        cart = new CartDefault(UUID.randomUUID());
        order = new OrderDefault(UUID.randomUUID());
    }

    // GreenPath Tests

    @Test
    void createNewOrder_shouldCreateOrderAndPublishEvent() {
        // When
        Order result = orderService.createNewOrder(cart);

        // Then
        assertNotNull(result);
        assertNotNull(result.id());
        verify(eventPublisher).publishEvent(any(OrderCreateEvent.class));
    }

    @Test
    void payOrder_shouldPublishPayEvent() {
        // When
        orderService.payOrder(order);

        // Then
        verify(eventPublisher).publishEvent(any(OrderPayEvent.class));
    }

    @Test
    void cancelOrder_shouldPublishCancelEvent() {
        // When
        orderService.cancelOrder(order);

        // Then
        verify(eventPublisher).publishEvent(any(OrderCancelEvent.class));
    }

    @Test
    void deliverOrder_shouldPublishDeliverEvent() {
        // When
        orderService.deliverOrder(order);

        // Then
        verify(eventPublisher).publishEvent(any(OrderDeliverEvent.class));
    }

    // RedPath Tests

    @Test
    void createNewOrder_whenCartIsNull_shouldReturnNullOrder() {
        // When
        Order result = orderService.createNewOrder(CartNullObject.getInstance());
        
        // Then
        // Verify that a NullOrder is returned and no event is published
        assertTrue(result.isNull());
        assertEquals(OrderNullObject.getInstance(), result);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void payOrder_whenOrderIsNull_shouldReturnWithoutException() {
        // When
        orderService.payOrder(OrderNullObject.getInstance());
        
        // Then
        // Verify that no event is published
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void cancelOrder_whenOrderIsNull_shouldReturnWithoutException() {
        // When
        orderService.cancelOrder(OrderNullObject.getInstance());
        
        // Then
        // Verify that no event is published
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void deliverOrder_whenOrderIsNull_shouldReturnWithoutException() {
        // When
        orderService.deliverOrder(OrderNullObject.getInstance());
        
        // Then
        // Verify that no event is published
        verifyNoInteractions(eventPublisher);
    }
}