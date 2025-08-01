package de.burger.it.application.dispatcher;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StateEventDispatcher {

    private final Map<Object, Map<Class<?>, List<StateEventHandler<?, ?>>>> registry = new HashMap<>();

    public StateEventDispatcher(List<StateEventHandler<?, ?>> handlers) {
        for (StateEventHandler<?, ?> handler : handlers) {
            for (Object stateKey : handler.supportedStates()) {
                registry
                        .computeIfAbsent(stateKey, s -> new HashMap<>())
                        .computeIfAbsent(handler.supportedEvent(), e -> new ArrayList<>())
                        .add(handler);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <S, E> void dispatch(S state, E event) {
        var eventMap = registry.get(state);
        if (eventMap == null) return;

        var handlers = eventMap.get(event.getClass());
        if (handlers == null) return;

        for (var raw : handlers) {
            ((StateEventHandler<S, E>) raw).execute(event);
        }
    }
}
