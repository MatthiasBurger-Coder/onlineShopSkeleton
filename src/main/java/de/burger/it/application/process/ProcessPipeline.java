package de.burger.it.application.process;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProcessPipeline<E> {
    private final List<Consumer<E>> steps = new ArrayList<>();

    public ProcessPipeline<E> append(Consumer<E> step) {
        steps.add(step);
        return this;
    }

    public void execute(E event) {
        for (Consumer<E> step : steps) {
            step.accept(event);
        }
    }
}

