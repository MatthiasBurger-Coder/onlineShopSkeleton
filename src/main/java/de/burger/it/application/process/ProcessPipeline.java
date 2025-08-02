package de.burger.it.application.process;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A generic pipeline for processing events or data.
 * This class provides a fluent API for building and executing processing pipelines.
 * 
 * @param <E> the type of data being processed
 */
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
     * Appends a function that transforms the input and returns a new output.
     * This method is an alias for append(UnaryOperator) with a more explicit name.
     * 
     * @param function the function to append
     * @return a new ProcessPipeline with the appended step
     */
    public ProcessPipeline<E> appendFunction(UnaryOperator<E> function) {
        return append(function);
    }

    /**
     * Appends a consumer that performs an action on the input but doesn't transform it.
     * This method is an alias for append(Consumer) with a more explicit name.
     * 
     * @param consumer the consumer to append
     * @return a new ProcessPipeline with the appended step
     */
    public ProcessPipeline<E> appendConsumer(Consumer<E> consumer) {
        return append(consumer);
    }
    
    /**
     * Appends a conditional step that only executes if the condition is true.
     * 
     * @param condition the condition to check
     * @param step the step to execute if the condition is true
     * @return a new ProcessPipeline with the conditional step appended
     */
    public ProcessPipeline<E> appendIf(Predicate<E> condition, UnaryOperator<E> step) {
        return append(e -> condition.test(e) ? step.apply(e) : e);
    }
    
    /**
     * Appends a conditional step that only executes if the condition is true.
     * 
     * @param condition the condition to check
     * @param consumer the consumer to execute if the condition is true
     * @return a new ProcessPipeline with the conditional step appended
     */
    public ProcessPipeline<E> appendIf(Predicate<E> condition, Consumer<E> consumer) {
        return appendIf(condition, e -> {
            consumer.accept(e);
            return e;
        });
    }
    
    /**
     * Composes this pipeline with another pipeline, creating a new pipeline that
     * applies this pipeline first, then the other pipeline.
     * 
     * @param after the pipeline to apply after this pipeline
     * @return a new pipeline that applies this pipeline first, then the other pipeline
     */
    public ProcessPipeline<E> andThen(ProcessPipeline<E> after) {
        List<Function<E, E>> newSteps = new ArrayList<>(steps);
        newSteps.add(after::execute);
        return new ProcessPipeline<>(newSteps);
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

