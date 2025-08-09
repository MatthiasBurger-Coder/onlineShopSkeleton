package de.burger.it.domain.order.model;

import java.util.UUID;

public record Order(UUID id) implements OrderLike {
    

}
