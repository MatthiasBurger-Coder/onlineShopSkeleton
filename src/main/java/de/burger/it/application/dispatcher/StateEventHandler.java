package de.burger.it.application.dispatcher;

import java.util.Collection;

public interface StateEventHandler<S, E> {
    Collection<S> supportedStates();
    Class<E> supportedEvent();
    void execute(E event);
}
