package de.burger.it.application.cart.service;

import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartNullObject;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartState;
import de.burger.it.domain.cart.state.CartStateType;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.NullCustomer;
import de.burger.it.domain.relation.model.CartCustomerAssignment;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private CartRepositoryPort cartRepository;

    @Mock
    private CartStatusAssignmentPort cartStatusAssignmentPort;

    @Mock
    private CartCustomerAssignmentPort cartCustomerAssignmentPort;

    private CartService cartService;
    private CartDefault cart;
    private Customer customer;

    @BeforeEach
    void setUp() {
        cartService = new CartService(publisher, cartRepository, cartStatusAssignmentPort, cartCustomerAssignmentPort);
        cart = new de.burger.it.domain.cart.model.CartDefault(UUID.randomUUID());
        customer = new Customer(UUID.randomUUID(), "Test Customer", "test@example.com");
    }

    // GreenPath Tests

    @Test
    void create_shouldCreateCartAndPublishEvent() {
        // When
        cartService.create(customer);

        // Then
        verify(publisher).publishEvent(any(CartCreateEvent.class));
    }

    @Test
    void close_shouldPublishCloseEvent() {
        // When
        cartService.close(cart, customer);

        // Then
        verify(publisher).publishEvent(any(CartCloseEvent.class));
    }

    @Test
    void activate_shouldPublishActiveEvent() {
        // When
        cartService.activate(cart, customer);

        // Then
        verify(publisher).publishEvent(any(CartActiveEvent.class));
    }

    @Test
    void findById_shouldReturnCartFromRepository() {
        // Given
        when(cartRepository.findById(cart.id())).thenReturn(cart);

        // When
        Cart result = cartService.findById(cart.id());

        // Then
        assertEquals(cart, result);
        verify(cartRepository).findById(cart.id());
    }

    @Test
    void findAllCartByCustomer_shouldReturnCartsForCustomer() {
        // Given
        UUID customerId = customer.id();
        UUID cartId = cart.id();
        CartCustomerAssignment assignment = new CartCustomerAssignment(cartId, customerId);
        
        when(cartCustomerAssignmentPort.findAllByCustomer(customerId)).thenReturn(List.of(assignment));
        when(cartRepository.findById(cartId)).thenReturn(cart);

        // When
        List<Cart> result = cartService.findAllCartByCustomer(customer);

        // Then
        assertEquals(1, result.size());
        assertEquals(cart, result.getFirst());
        verify(cartCustomerAssignmentPort).findAllByCustomer(customerId);
        verify(cartRepository).findById(cartId);
    }

    @Test
    void findAllCartsByCart_shouldReturnCartsForCart() {
        // Given
        UUID cartId = cart.id();
        CartCustomerAssignment assignment = new CartCustomerAssignment(cartId, customer.id());
        
        when(cartCustomerAssignmentPort.findAllByCart(cartId)).thenReturn(List.of(assignment));
        when(cartRepository.findById(cartId)).thenReturn(cart);

        // When
        List<Cart> result = cartService.findAllCartsByCart(cart);

        // Then
        assertEquals(1, result.size());
        assertEquals(cart, result.getFirst());
        verify(cartCustomerAssignmentPort).findAllByCart(cartId);
        verify(cartRepository).findById(cartId);
    }

    @Test
    void getState_shouldReturnCartState() {
        // Given
        CartStateType stateType = CartStateType.CREATED;
        when(cartStatusAssignmentPort.findBy(cart.id())).thenReturn(stateType);
        
        // When
        CartState result = cartService.getState(cart);

        // Then
        // We can't directly verify the result since we can't mock the enum's toState method
        // Instead, we verify that the correct method was called on the port
        verify(cartStatusAssignmentPort).findBy(cart.id());
        assertNotNull(result);
    }

    // RedPath Tests

    @Test
    void create_whenCustomerIsNull_shouldReturnWithoutException() {
        // When
        cartService.create(NullCustomer.getInstance());

        // Then
        // Verify that no event is published
        verifyNoInteractions(publisher);
    }

    @Test
    void close_whenCartIsNull_shouldReturnWithoutException() {
        // When
        cartService.close(CartNullObject.getInstance(), customer);

        // Then
        // Verify that no event is published
        verifyNoInteractions(publisher);
    }

    @Test
    void close_whenCustomerIsNull_shouldReturnWithoutException() {
        // When
        cartService.close(cart, NullCustomer.getInstance());

        // Then
        // Verify that no event is published
        verifyNoInteractions(publisher);
    }

    @Test
    void activate_whenCartIsNull_shouldReturnWithoutException() {
        // When
        cartService.activate(CartNullObject.getInstance(), customer);

        // Then
        // Verify that no event is published
        verifyNoInteractions(publisher);
    }

    @Test
    void activate_whenCustomerIsNull_shouldReturnWithoutException() {
        // When
        cartService.activate(cart, NullCustomer.getInstance());

        // Then
        // Verify that no event is published
        verifyNoInteractions(publisher);
    }

    @Test
    void findById_whenIdIsNull_shouldReturnNullCart() {
        // When
        Cart result = cartService.findById(null);

        // Then
        assertTrue(result.isNull());
        assertEquals(CartNullObject.getInstance(), result);
    }

    @Test
    void findAllCartByCustomer_whenCustomerIsNull_shouldReturnEmptyList() {
        // When
        List<Cart> result = cartService.findAllCartByCustomer(NullCustomer.getInstance());

        // Then
        assertTrue(result.isEmpty());
        verifyNoInteractions(cartCustomerAssignmentPort);
    }

    @Test
    void findAllCartsByCart_whenCartIsNull_shouldReturnEmptyList() {
        // When
        List<Cart> result = cartService.findAllCartsByCart(CartNullObject.getInstance());

        // Then
        assertTrue(result.isEmpty());
        verifyNoInteractions(cartCustomerAssignmentPort);
    }

    @Test
    void getState_whenCartIsNull_shouldReturnNullState() {
        // When
        var nullChart = new CartNullObject();
        CartState result = cartService.getState(CartNullObject.getInstance());

        // Then
        assertEquals(nullChart, result.notDefined());
        verifyNoInteractions(cartStatusAssignmentPort);
    }

    @Test
    void findAllCartByCustomer_whenNoAssignmentsFound_shouldReturnEmptyList() {
        // Given
        when(cartCustomerAssignmentPort.findAllByCustomer(customer.id())).thenReturn(Collections.emptyList());

        // When
        List<Cart> result = cartService.findAllCartByCustomer(customer);

        // Then
        assertTrue(result.isEmpty());
        verify(cartCustomerAssignmentPort).findAllByCustomer(customer.id());
        verifyNoInteractions(cartRepository);
    }

    @Test
    void findById_whenRepositoryThrowsException_shouldPropagateException() {
        // Given
        when(cartRepository.findById(cart.id())).thenThrow(new RuntimeException("Repository error"));

        // When/Then
        Exception exception = assertThrows(RuntimeException.class, () -> cartService.findById(cart.id()));
        assertEquals("Repository error", exception.getMessage());
    }
}