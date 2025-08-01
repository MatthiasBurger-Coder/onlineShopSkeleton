package de.burger.it.application.cart.service;

import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartState;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import lombok.ToString;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@ToString
public class CartService {

    private final ApplicationEventPublisher publisher;

    private final CartStatusAssignmentPort cartStatusAssignmentPort;
    private final CartCustomerAssignmentPort cartCustomerAssignmentPort;
    private final CartRepositoryPort cartRepository;

    public CartService(ApplicationEventPublisher publisher, CartRepositoryPort cartRepository,
                       CartStatusAssignmentPort cartStatusAssignmentPort,
                       CartCustomerAssignmentPort cartCustomerAssignmentPort) {
        this.publisher = publisher;
        this.cartRepository = cartRepository;
        this.cartStatusAssignmentPort = cartStatusAssignmentPort;
        this.cartCustomerAssignmentPort = cartCustomerAssignmentPort;

    }

    public void create(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        var cart = new Cart(UUID.randomUUID());
        var cartCreateEvent = new CartCreateEvent(cart, customer);
        publisher.publishEvent(cartCreateEvent);
    }

    public void close(Cart cart, Customer customer) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        var cartCloseEvent = new CartCloseEvent(cart, customer);
        publisher.publishEvent(cartCloseEvent);
    }

    public void activate(Cart cart, Customer customer) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        var cartActiveEvent = new CartActiveEvent(cart, customer);
        publisher.publishEvent(cartActiveEvent);
    }

    public Cart findById(UUID cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        return cartRepository.findById(cartId);
    }

    public List<Cart> findAllCartByCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        var cartCustomerAssignments = cartCustomerAssignmentPort.findAllByCustomer(customer.id());
        return cartCustomerAssignments.stream().map(cartCustomerAssignment -> cartRepository.findById(cartCustomerAssignment.cartId())).toList();
    }

    public List<Cart> findAllCartByCarts(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        var cartCustomerAssignments = cartCustomerAssignmentPort.findAllByCard(cart.id());
        return cartCustomerAssignments.stream().map(cartCustomerAssignment -> cartRepository.findById(cartCustomerAssignment.cartId())).toList();
    }

    public CartState getState(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        return cartStatusAssignmentPort.findBy(cart.id()).toState();
    }

}
