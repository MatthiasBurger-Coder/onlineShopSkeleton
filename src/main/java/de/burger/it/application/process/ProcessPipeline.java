package de.burger.it.application.process;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ProcessPipeline<E> {
    private final List<Function<E, E>> steps;

    // Private constructor to ensure immutability
    private ProcessPipeline(List<Function<E, E>> steps) {
        this.steps = new ArrayList<>(steps); // Create a defensive copy
    }

    // Public constructor for creating a new empty pipeline
    public ProcessPipeline() {
        this.steps = new ArrayList<>();
    }

    /**
     * Appends a function that transforms the input and returns a new output
     * @param step the function to append
     * @return a new ProcessPipeline with the appended step
     */
    public ProcessPipeline<E> append(UnaryOperator<E> step) {
        List<Function<E, E>> newSteps = new ArrayList<>(steps);
        newSteps.add(step);
        return new ProcessPipeline<>(newSteps);
    }

    /**
     * Appends a consumer that performs an action on the input but doesn't transform it
     * This maintains compatibility with existing code
     * @param step the consumer to append
     * @return a new ProcessPipeline with the appended step
     */
    public ProcessPipeline<E> append(Consumer<E> step) {
        return append(e -> {
            step.accept(e);
            return e;
        });
    }

    /**
     * Executes the pipeline by applying each step in sequence
     * @param event the input event
     * @return the transformed event after all steps have been applied
     */
    public E execute(E event) {
        E result = event;
        for (Function<E, E> step : steps) {
            result = step.apply(result);
        }
        return result;
    }
}

