package de.burger.it.application.order.process;

import de.burger.it.application.config.AppConfig;
import de.burger.it.application.process.ProcessPipeline;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.port.OrderRepositoryPort;
import de.burger.it.domain.order.port.OrderStatusAssignmentPort;
import de.burger.it.domain.order.state.OrderStateType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessConfigTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("GreenPath: orderCreate pipeline assigns NEW state and saves order")
    void greenPath_orderCreatePipeline_assignsAndSaves() {
        // Given a Spring context with component scan and process configuration
        context = new AnnotationConfigApplicationContext(AppConfig.class, OrderProcessConfig.class);

        // And a fresh order
        Order order = new OrderDefault(UUID.randomUUID());
        OrderCreateEvent event = new OrderCreateEvent(order);

        // When executing the pipeline
        @SuppressWarnings("unchecked")
        ProcessPipeline<OrderCreateEvent> pipeline = (ProcessPipeline<OrderCreateEvent>) context.getBean("orderCreateProcessPipeline");
        OrderCreateEvent result = pipeline.execute(event);

        // Then the same event should be returned (pipeline passes through)
        assertSame(event, result, "Pipeline should return the same event instance for consumer steps");

        // And the order should be assigned NEW state and saved into repository
        OrderStatusAssignmentPort statusPort = context.getBean(OrderStatusAssignmentPort.class);
        OrderRepositoryPort repositoryPort = context.getBean(OrderRepositoryPort.class);

        assertEquals(OrderStateType.NEW, statusPort.findBy(order.id()), "Order should be assigned NEW state");
        assertSame(order, repositoryPort.findById(order.id()), "Order should be saved in repository");
    }

    @Test
    @DisplayName("RedPath: orderCreate pipeline throws when event has null order")
    void redPath_orderCreatePipeline_throwsOnNullOrder() {
        // Given a Spring context with process configuration
        context = new AnnotationConfigApplicationContext(AppConfig.class, OrderProcessConfig.class);

        // And an event with null order
        OrderCreateEvent badEvent = new OrderCreateEvent(null);

        // When / Then executing the pipeline should throw due to null order in handler
        @SuppressWarnings("unchecked")
        ProcessPipeline<OrderCreateEvent> pipeline = (ProcessPipeline<OrderCreateEvent>) context.getBean("orderCreateProcessPipeline");
        assertThrows(IllegalArgumentException.class, () -> pipeline.execute(badEvent),
                "Pipeline should propagate IllegalArgumentException when order is null");
    }
}
