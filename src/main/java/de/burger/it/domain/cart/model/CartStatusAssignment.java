package de.burger.it.domain.cart.model;

import de.burger.it.domain.cart.state.CartStateType;

import java.time.LocalDateTime;
import java.util.UUID;


public record CartStatusAssignment(UUID cartId,
                                   CartStateType state,
                                   LocalDateTime from,
                                   LocalDateTime to) {
}