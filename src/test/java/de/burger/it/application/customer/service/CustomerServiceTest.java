package de.burger.it.application.customer.service;

import de.burger.it.application.cart.service.CartService;
import de.burger.it.domain.customer.event.CustomerCreateEvent;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerState;
import de.burger.it.domain.customer.state.CustomerStateType;
import de.burger.it.domain.cart.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private CustomerStatusAssignmentPort customerStatusAssignmentPort;

    @Mock
    private CartService cartService;

    private CustomerService customerService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerStatusAssignmentPort, publisher, cartService);
        customer = new Customer(UUID.randomUUID(), "Test Customer", "test@example.com");
    }

    // GreenPath Tests

    @Test
    void createNewCustomer_shouldAssignCreateStateAndPublishEvent() {
        // When
        customerService.createNewCustomer(customer);

        // Then
        verify(customerStatusAssignmentPort).assign(customer, CustomerStateType.CREATE);
        verify(publisher).publishEvent(any(CustomerCreateEvent.class));
    }

    @Test
    void suspendCustomer_shouldPublishEventAndCloseAllCarts() {
        // Given
        Cart cart1 = new Cart(UUID.randomUUID());
        Cart cart2 = new Cart(UUID.randomUUID());
        List<Cart> carts = List.of(cart1, cart2);
        
        when(cartService.findAllCartByCustomer(customer)).thenReturn(carts);

        // When
        customerService.suspendCustomer(customer);

        // Then
        verify(publisher).publishEvent(any(CustomerSuspendEvent.class));
        verify(cartService).findAllCartByCustomer(customer);
        verify(cartService).close(cart1, customer);
        verify(cartService).close(cart2, customer);
    }

    @Test
    void getState_shouldReturnCustomerState() {
        // Given
        CustomerStateType stateType = CustomerStateType.ACTIVE;
        when(customerStatusAssignmentPort.findBy(customer.id())).thenReturn(stateType);
        
        // When
        CustomerState result = customerService.getState(customer);

        // Then
        // We can't directly verify the result since we can't mock the enum's toState method
        // Instead, we verify that the correct method was called on the port
        verify(customerStatusAssignmentPort).findBy(customer.id());
        assertNotNull(result);
        assertEquals(stateType, result.code());
    }

    // RedPath Tests

    @Test
    void createNewCustomer_whenCustomerIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> customerService.createNewCustomer(null));
    }

    @Test
    void suspendCustomer_whenCustomerIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> customerService.suspendCustomer(null));
    }

    @Test
    void getState_whenCustomerIsNull_shouldThrowException() {
        // When/Then
        // Verify that some exception is thrown when passing null
        assertThrows(Exception.class, () -> customerService.getState(null));
    }

    @Test
    void suspendCustomer_whenCartServiceThrowsException_shouldPropagateException() {
        // Given
        when(cartService.findAllCartByCustomer(customer)).thenThrow(new RuntimeException("Cart service error"));

        // When/Then
        Exception exception = assertThrows(RuntimeException.class, () -> customerService.suspendCustomer(customer));
        assertEquals("Cart service error", exception.getMessage());
        verify(publisher).publishEvent(any(CustomerSuspendEvent.class));
    }

    @Test
    void getState_whenStatusAssignmentPortThrowsException_shouldPropagateException() {
        // Given
        when(customerStatusAssignmentPort.findBy(customer.id())).thenThrow(new RuntimeException("Status assignment error"));

        // When/Then
        Exception exception = assertThrows(RuntimeException.class, () -> customerService.getState(customer));
        assertEquals("Status assignment error", exception.getMessage());
    }
}