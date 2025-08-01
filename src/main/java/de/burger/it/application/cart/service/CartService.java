package de.burger.it.application.cart.service;

import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartLike;
import de.burger.it.domain.cart.model.NullCart;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartState;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerLike;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import lombok.ToString;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public void create(CustomerLike customer) {
        if (customer.isNull()) {
            return;
        }
        var cart = new Cart(UUID.randomUUID());
        var cartCreateEvent = new CartCreateEvent(cart, (Customer) customer);
        publisher.publishEvent(cartCreateEvent);
    }

    public void close(CartLike cart, CustomerLike customer) {
        if (cart.isNull() || customer.isNull()) {
            return;
        }
        var cartCloseEvent = new CartCloseEvent((Cart) cart, (Customer) customer);
        publisher.publishEvent(cartCloseEvent);
    }

    public void activate(CartLike cart, CustomerLike customer) {
        if (cart.isNull() || customer.isNull()) {
            return;
        }
        var cartActiveEvent = new CartActiveEvent((Cart) cart, (Customer) customer);
        publisher.publishEvent(cartActiveEvent);
    }

    public CartLike findById(UUID cartId) {
        if (cartId == null) {
            return NullCart.getInstance();
        }
        CartLike cart = cartRepository.findById(cartId);
        return cart != null ? cart : NullCart.getInstance();
    }

    public List<CartLike> findAllCartByCustomer(CustomerLike customer) {
        if (customer.isNull()) {
            return Collections.emptyList();
        }
        var cartCustomerAssignments = cartCustomerAssignmentPort.findAllByCustomer(customer.id());
        return cartCustomerAssignments.stream()
                .map(cartCustomerAssignment -> {
                    CartLike cart = cartRepository.findById(cartCustomerAssignment.cartId());
                    return cart != null ? cart : NullCart.getInstance();
                })
                .toList();
    }

    public List<CartLike> findAllCartByCarts(CartLike cart) {
        if (cart.isNull()) {
            return Collections.emptyList();
        }
        var cartCustomerAssignments = cartCustomerAssignmentPort.findAllByCard(cart.id());
        return cartCustomerAssignments.stream()
                .map(cartCustomerAssignment -> {
                    CartLike foundCart = cartRepository.findById(cartCustomerAssignment.cartId());
                    return foundCart != null ? foundCart : NullCart.getInstance();
                })
                .toList();
    }

    public CartState getState(CartLike cart) {
        if (cart.isNull()) {
            return null; // Return null or a default state for NullCart
        }
        return cartStatusAssignmentPort.findBy(cart.id()).toState();
    }

}
