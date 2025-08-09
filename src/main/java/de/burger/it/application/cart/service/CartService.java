package de.burger.it.application.cart.service;

import de.burger.it.domain.cart.event.CartActiveEvent;
import de.burger.it.domain.cart.event.CartCloseEvent;
import de.burger.it.domain.cart.event.CartCreateEvent;
import de.burger.it.domain.cart.model.Cart;
import de.burger.it.domain.cart.model.CartDefault;
import de.burger.it.domain.cart.model.CartNullObject;
import de.burger.it.domain.cart.port.CartRepositoryPort;
import de.burger.it.domain.cart.port.CartStatusAssignmentPort;
import de.burger.it.domain.cart.state.CartState;
import de.burger.it.domain.cart.state.CartStateType;
import de.burger.it.domain.cart.state.NullCartState;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.relation.port.CartCustomerAssignmentPort;
import lombok.ToString;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .ifPresent(c -> {
                    var cart = new CartDefault(UUID.randomUUID());
                    var cartCreateEvent = new CartCreateEvent(cart, c);
                    publisher.publishEvent(cartCreateEvent);
                });
    }

    public void close(Cart cart, Customer customer) {
        Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .ifPresent(c -> Optional.ofNullable(customer)
                        .filter(cu -> !cu.isNull())
                        .ifPresent(cu -> {
                            var cartCloseEvent = new CartCloseEvent(c, cu);
                            publisher.publishEvent(cartCloseEvent);
                        }));
    }

    public void activate(Cart cart, Customer customer) {
        Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .ifPresent(c -> Optional.ofNullable(customer)
                        .filter(cu -> !cu.isNull())
                        .ifPresent(cu -> {
                            var cartActiveEvent = new CartActiveEvent(c, cu);
                            publisher.publishEvent(cartActiveEvent);
                        }));
    }

    public Cart findById(UUID cartId) {
        return Optional.ofNullable(cartId)
                .map(cartRepository::findById)
                .orElse(CartNullObject.getInstance());
    }

    public List<Cart> findAllCartByCustomer(Customer customer) {
        return Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .map(c -> cartCustomerAssignmentPort.findAllByCustomer(c.id()))
                .orElse(Collections.emptyList())
                .stream()
                .map(cartCustomerAssignment -> Optional.ofNullable(cartRepository.findById(cartCustomerAssignment.cartId()))
                        .orElse(CartNullObject.getInstance()))
                .toList();
    }

    public List<Cart> findAllCartsByCart(Cart cart) {
        return Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .map(c -> cartCustomerAssignmentPort.findAllByCart(c.id()))
                .orElse(Collections.emptyList())
                .stream()
                .map(cartCustomerAssignment -> Optional.ofNullable(cartRepository.findById(cartCustomerAssignment.cartId()))
                        .orElse(CartNullObject.getInstance()))
                .toList();
    }

    public CartState getState(Cart cart) {
        return Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .map(c -> cartStatusAssignmentPort.findBy(c.id()))
                .map(CartStateType::toState)
                .orElse(new NullCartState());
    }

}
