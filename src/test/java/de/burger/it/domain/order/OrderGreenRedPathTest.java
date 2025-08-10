package de.burger.it.domain.order;

import de.burger.it.domain.order.event.OrderCancelEvent;
import de.burger.it.domain.order.event.OrderCreateEvent;
import de.burger.it.domain.order.event.OrderDeliverEvent;
import de.burger.it.domain.order.event.OrderPayEvent;
import de.burger.it.domain.order.model.Order;
import de.burger.it.domain.order.model.OrderDefault;
import de.burger.it.domain.order.model.OrderNullObject;
import de.burger.it.domain.order.model.OrderStatusAssignment;
import de.burger.it.domain.order.state.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderGreenRedPathTest {

    @Test
    @DisplayName("Greenpath: OrderDefault basic behavior and id propagation; Order interface default isNull=false")
    void orderDefault_basic_greenpath() {
        UUID id = UUID.randomUUID();
        Order order = new OrderDefault(id);
        assertEquals(id, order.id());
        assertFalse(order.isNull(), "OrderDefault should not be null-object");

        // Also check via Order interface default impl works for non-null implementations
        Order adhoc = () -> id;
        assertFalse(adhoc.isNull());
    }

    @Test
    @DisplayName("Greenpath: OrderNullObject singleton, id is zero-UUID, isNull=true, same instance each time")
    void orderNullObject_singleton_greenpath() {
        Order a = OrderNullObject.getInstance();
        Order b = OrderNullObject.getInstance();
        assertSame(a, b, "OrderNullObject should be a singleton");
        assertTrue(a.isNull(), "Null object should report isNull=true");
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), a.id());
    }

    @Test
    @DisplayName("Redpath: OrderNullObject equals(null) and equals(unrelated) should be false")
    void orderNullObject_equals_redpath() {
        OrderNullObject nullOrder = (OrderNullObject) OrderNullObject.getInstance();
        assertNotEquals(nullOrder, null);
        assertNotEquals(nullOrder, new Object());
    }

    // State: NewState
    @Test
    @DisplayName("Greenpath: NewState.pay -> PaidState, NewState.cancel -> CanceledState")
    void newState_validTransitions_greenpath() {
        OrderState s = new NewState();
        assertTrue(s.pay() instanceof PaidState);
        assertTrue(s.cancel() instanceof CanceledState);
    }

    @Test
    @DisplayName("Redpath: NewState.deliver throws 'OrderDefault not paid'")
    void newState_invalidDeliver_redpath() {
        OrderState s = new NewState();
        IllegalStateException ex = assertThrows(IllegalStateException.class, s::deliver);
        assertEquals("OrderDefault not paid", ex.getMessage());
    }

    // State: PaidState
    @Test
    @DisplayName("Greenpath: PaidState.pay returns same instance (idempotent), deliver -> DeliveredState")
    void paidState_validTransitions_greenpath() {
        PaidState paid = new PaidState();
        assertSame(paid, paid.pay(), "pay on PaidState should return same instance");
        assertTrue(paid.deliver() instanceof DeliveredState);
    }

    @Test
    @DisplayName("Redpath: PaidState.cancel throws 'Can't cancel paid order'")
    void paidState_invalidCancel_redpath() {
        OrderState s = new PaidState();
        IllegalStateException ex = assertThrows(IllegalStateException.class, s::cancel);
        assertEquals("Can't cancel paid order", ex.getMessage());
    }

    // State: DeliveredState
    @Test
    @DisplayName("Greenpath: DeliveredState.deliver returns same instance")
    void deliveredState_validTransition_greenpath() {
        DeliveredState delivered = new DeliveredState();
        assertSame(delivered, delivered.deliver());
    }

    @Test
    @DisplayName("Redpath: DeliveredState.cancel throws 'Can't be cancelled', pay throws 'Already paid'")
    void deliveredState_invalidTransitions_redpath() {
        OrderState s = new DeliveredState();
        IllegalStateException ex1 = assertThrows(IllegalStateException.class, s::cancel);
        assertEquals("Can't be cancelled", ex1.getMessage());
        IllegalStateException ex2 = assertThrows(IllegalStateException.class, s::pay);
        assertEquals("Already paid", ex2.getMessage());
    }

    // State: CanceledState
    @Test
    @DisplayName("Greenpath: CanceledState.cancel returns same instance")
    void canceledState_validTransition_greenpath() {
        CanceledState canceled = new CanceledState();
        assertSame(canceled, canceled.cancel());
    }

    @Test
    @DisplayName("Redpath: CanceledState.pay and deliver both throw 'Already canceled'")
    void canceledState_invalidTransitions_redpath() {
        OrderState s = new CanceledState();
        IllegalStateException ex1 = assertThrows(IllegalStateException.class, s::pay);
        assertEquals("Already canceled", ex1.getMessage());
        IllegalStateException ex2 = assertThrows(IllegalStateException.class, s::deliver);
        assertEquals("Already canceled", ex2.getMessage());
    }

    // OrderStateType mapping and code()
    @Test
    @DisplayName("Greenpath: OrderStateType.toState returns correct classes and code() matches type")
    void orderStateType_mapping_greenpath() {
        assertInstanceOf(NewState.class, OrderStateType.NEW.toState());
        assertEquals(OrderStateType.NEW, OrderStateType.NEW.toState().code());

        assertInstanceOf(PaidState.class, OrderStateType.PAID.toState());
        assertEquals(OrderStateType.PAID, OrderStateType.PAID.toState().code());

        assertInstanceOf(CanceledState.class, OrderStateType.CANCELLED.toState());
        assertEquals(OrderStateType.CANCELLED, OrderStateType.CANCELLED.toState().code());

        assertInstanceOf(DeliveredState.class, OrderStateType.DELIVERED.toState());
        assertEquals(OrderStateType.DELIVERED, OrderStateType.DELIVERED.toState().code());
    }

    // Events
    @Test
    @DisplayName("Greenpath: Order events expose provided Order instance")
    void orderEvents_greenpath() {
        Order order = new OrderDefault(UUID.randomUUID());
        assertSame(order, new OrderCreateEvent(order).order());
        assertSame(order, new OrderPayEvent(order).order());
        assertSame(order, new OrderDeliverEvent(order).order());
        assertSame(order, new OrderCancelEvent(order).order());
    }

    // OrderStatusAssignment
    @Test
    @DisplayName("Greenpath: OrderStatusAssignment holds orderId and state")
    void orderStatusAssignment_greenpath() {
        UUID id = UUID.randomUUID();
        OrderStatusAssignment osa = new OrderStatusAssignment(id, OrderStateType.PAID);
        assertEquals(id, osa.orderId());
        assertEquals(OrderStateType.PAID, osa.state());
    }
}
