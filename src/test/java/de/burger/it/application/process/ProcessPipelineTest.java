package de.burger.it.application.process;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
}
