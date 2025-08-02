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
import de.burger.it.domain.cart.state.CartStateType;
import de.burger.it.domain.cart.state.NullCartState;
import de.burger.it.domain.customer.model.Customer;
import de.burger.it.domain.customer.model.CustomerLike;
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

    public void create(CustomerLike customer) {
        Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .ifPresent(c -> {
                    var cart = new Cart(UUID.randomUUID());
                    var cartCreateEvent = new CartCreateEvent(cart, (Customer) c);
                    publisher.publishEvent(cartCreateEvent);
                });
    }

    public void close(CartLike cart, CustomerLike customer) {
        Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .ifPresent(c -> Optional.ofNullable(customer)
                        .filter(cu -> !cu.isNull())
                        .ifPresent(cu -> {
                            var cartCloseEvent = new CartCloseEvent((Cart) c, (Customer) cu);
                            publisher.publishEvent(cartCloseEvent);
                        }));
    }

    public void activate(CartLike cart, CustomerLike customer) {
        Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .ifPresent(c -> Optional.ofNullable(customer)
                        .filter(cu -> !cu.isNull())
                        .ifPresent(cu -> {
                            var cartActiveEvent = new CartActiveEvent((Cart) c, (Customer) cu);
                            publisher.publishEvent(cartActiveEvent);
                        }));
    }

    public CartLike findById(UUID cartId) {
        return Optional.ofNullable(cartId)
                .map(cartRepository::findById)
                .orElse(NullCart.getInstance());
    }

    public List<CartLike> findAllCartByCustomer(CustomerLike customer) {
        return Optional.ofNullable(customer)
                .filter(c -> !c.isNull())
                .map(c -> cartCustomerAssignmentPort.findAllByCustomer(c.id()))
                .orElse(Collections.emptyList())
                .stream()
                .map(cartCustomerAssignment -> Optional.ofNullable(cartRepository.findById(cartCustomerAssignment.cartId()))
                        .orElse(NullCart.getInstance()))
                .toList();
    }

    public List<CartLike> findAllCartsByCart(CartLike cart) {
        return Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .map(c -> cartCustomerAssignmentPort.findAllByCart(c.id()))
                .orElse(Collections.emptyList())
                .stream()
                .map(cartCustomerAssignment -> Optional.ofNullable(cartRepository.findById(cartCustomerAssignment.cartId()))
                        .orElse(NullCart.getInstance()))
                .toList();
    }

    public CartState getState(CartLike cart) {
        return Optional.ofNullable(cart)
                .filter(c -> !c.isNull())
                .map(c -> cartStatusAssignmentPort.findBy(c.id()))
                .map(CartStateType::toState)
                .orElse(NullCartState.getInstance());
    }

}
