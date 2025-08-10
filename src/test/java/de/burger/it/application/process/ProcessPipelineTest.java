package de.burger.it.application.process;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPipelineTest {

    @Test
    void execute_greenPath_shouldRunConsumersAndFunctionsInOrder_andReturnTransformedResult() {
        // Given
        ProcessPipeline<Integer> pipeline = new ProcessPipeline<>();

        @SuppressWarnings("unchecked")
        Consumer<Integer> c1 = (Consumer<Integer>) mock(Consumer.class);
        @SuppressWarnings("unchecked")
        Consumer<Integer> c2 = (Consumer<Integer>) mock(Consumer.class);
        @SuppressWarnings("unchecked")
        Consumer<Integer> c3 = (Consumer<Integer>) mock(Consumer.class);

        UnaryOperator<Integer> f1 = x -> x + 1;         // 1 -> 2
        UnaryOperator<Integer> f2 = x -> x * 2;         // 2 -> 4
        UnaryOperator<Integer> f3 = x -> x + 3;         // 4 -> 7

        ProcessPipeline<Integer> full = pipeline
                .append(c1)                             // accept(1)
                .append(f1)                             // -> 2
                .append(c2)                             // accept(2)
                .append(f2)                             // -> 4
                .appendIf(v -> true, c3)                // accept(4)
                .appendIf(v -> true, f3);               // -> 7

        int start = 1;

        // When
        int result = full.execute(start);

        // Then
        InOrder inOrder = inOrder(c1, c2, c3);
        org.mockito.ArgumentCaptor<Integer> captor = org.mockito.ArgumentCaptor.forClass(Integer.class);
        
        inOrder.verify(c1).accept(captor.capture());
        int c1Arg = captor.getAllValues().get(0);
        inOrder.verify(c2).accept(captor.capture());
        int c2Arg = captor.getAllValues().get(1);
        inOrder.verify(c3).accept(captor.capture());
        int c3Arg = captor.getAllValues().get(2);

        System.out.println("[DEBUG_LOG] c1Arg=" + c1Arg + ", c2Arg=" + c2Arg + ", c3Arg=" + c3Arg);
        assertEquals(1, c1Arg);
        assertEquals(2, c2Arg);
        assertEquals(4, c3Arg);

        assertEquals(7, result);
    }

    @Test
    void execute_redPath_whenAFunctionThrows_shouldNotExecuteSubsequentSteps_andPropagateException() {
        // Given
        ProcessPipeline<Integer> pipeline = new ProcessPipeline<>();

        @SuppressWarnings("unchecked")
        Consumer<Integer> c1 = (Consumer<Integer>) mock(Consumer.class);
        @SuppressWarnings("unchecked")
        Consumer<Integer> c2 = (Consumer<Integer>) mock(Consumer.class);

        @SuppressWarnings("unchecked")
        UnaryOperator<Integer> throwingFn = (UnaryOperator<Integer>) mock(UnaryOperator.class);
        @SuppressWarnings("unchecked")
        UnaryOperator<Integer> neverCalledFn = (UnaryOperator<Integer>) mock(UnaryOperator.class);

        int start = 10;

        doNothing().when(c1).accept(start);
        when(throwingFn.apply(start)).thenThrow(new IllegalStateException("boom"));

        ProcessPipeline<Integer> full = pipeline
                .append(c1)
                .append(throwingFn)
                .append(c2)
                .append(neverCalledFn);

        // When / Then
        assertThrows(IllegalStateException.class, () -> full.execute(start));

        verify(c1).accept(start);
        verify(throwingFn).apply(start);
        verifyNoInteractions(c2);
        verifyNoInteractions(neverCalledFn);
    }

    @Test
    void appendFunction_and_appendConsumer_aliases_shouldBehaveLike_append() {
        // Given
        List<Integer> sideEffects = new ArrayList<>();
        ProcessPipeline<Integer> pipeline = new ProcessPipeline<Integer>()
                .appendConsumer(sideEffects::add)  // record original value
                .appendFunction(v -> v + 5)        // transform
                .appendConsumer(sideEffects::add); // record transformed value

        // When
        int result = pipeline.execute(3);

        // Then
        assertEquals(3, sideEffects.get(0));
        assertEquals(8, sideEffects.get(1));
        assertEquals(8, result);
    }

    @Test
    void appendIf_falseBranch_shouldSkipConsumerAndFunction() {
        // Given
        @SuppressWarnings("unchecked")
        Consumer<Integer> consumer = (Consumer<Integer>) mock(Consumer.class);
        @SuppressWarnings("unchecked")
        UnaryOperator<Integer> function = (UnaryOperator<Integer>) mock(UnaryOperator.class);

        ProcessPipeline<Integer> pipeline = new ProcessPipeline<Integer>()
                .appendIf(v -> false, consumer)
                .appendIf(v -> false, function);

        // When
        int result = pipeline.execute(42);

        // Then
        assertEquals(42, result);
        verifyNoInteractions(consumer);
        verifyNoInteractions(function);
    }

    @Test
    void andThen_shouldComposeTwoPipelines_inOrder() {
        // Given
        AtomicInteger side = new AtomicInteger(0);
        ProcessPipeline<Integer> p1 = new ProcessPipeline<Integer>()
                .append(v -> v + 1)
                .appendConsumer(side::addAndGet); // after +1

        ProcessPipeline<Integer> p2 = new ProcessPipeline<Integer>()
                .append(v -> v * 3)
                .appendConsumer(side::addAndGet); // after *3

        ProcessPipeline<Integer> composed = p1.andThen(p2);

        // When
        int result = composed.execute(2); // ((2+1)=3) -> (3*3)=9

        // Then
        assertEquals(3 + 9, side.get());
        assertEquals(9, result);
    }

    @Test
    void emptyPipeline_shouldReturnInputUnchanged_sameReferenceForObjects() {
        // Given
        ProcessPipeline<Object> empty = new ProcessPipeline<>();
        Object obj = new Object();

        // When
        Object out = empty.execute(obj);

        // Then
        assertSame(obj, out);
    }

    @Test
    void append_shouldReturnNewPipeline_andNotMutateOriginal() {
        // Given
        ProcessPipeline<Integer> original = new ProcessPipeline<>();
        ProcessPipeline<Integer> modified = original.append(v -> v + 2);

        // When
        int originalResult = original.execute(5);
        int modifiedResult = modified.execute(5);

        // Then
        assertEquals(5, originalResult, "Original pipeline should remain empty and return input");
        assertEquals(7, modifiedResult, "Modified pipeline should apply transformation");
        assertNotSame(original, modified, "append should create a new pipeline instance");
    }
}
