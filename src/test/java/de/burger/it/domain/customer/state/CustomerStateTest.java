package de.burger.it.domain.customer.state;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerStateTest {

    @Test
    void createdState_greenPath_transitionsAndCode() {
        CustomerState created = new CreatedState();

        // create() returns same instance (id equality)
        CustomerState createdAgain = created.create();
        assertThat(createdAgain, sameInstance(created));

        // active() transitions to ActivatedState
        CustomerState activated = created.active();
        assertThat(activated, instanceOf(ActivatedState.class));

        // code()
        assertThat(created.code(), is(CustomerStateType.CREATE));

        // call toString() just to exercise Lombok-generated method
        assertThat(created.toString(), containsString("CreatedState"));
    }

    @Test
    void createdState_redPath_suspendThrows() {
        CustomerState created = new CreatedState();
        IllegalStateException ex = assertThrows(IllegalStateException.class, created::suspend);
        assertThat(ex.getMessage(), containsString("Cannot suspend created customer"));
    }

    @Test
    void activatedState_greenPath_transitionsAndCode() {
        CustomerState active = new ActivatedState();

        // suspend() transitions to SuspendedState
        CustomerState suspended = active.suspend();
        assertThat(suspended, instanceOf(SuspendedState.class));

        // active() returns same instance
        assertThat(active.active(), sameInstance(active));

        // code()
        assertThat(active.code(), is(CustomerStateType.ACTIVE));

        // toString coverage
        assertThat(active.toString(), containsString("ActivatedState"));
    }

    @Test
    void activatedState_redPath_createThrows() {
        CustomerState active = new ActivatedState();
        IllegalStateException ex = assertThrows(IllegalStateException.class, active::create);
        assertThat(ex.getMessage(), containsString("Cannot create customer in active state"));
    }

    @Test
    void suspendedState_greenPath_transitionsAndCode() {
        CustomerState suspended = new SuspendedState();

        // suspend() returns same instance
        assertThat(suspended.suspend(), sameInstance(suspended));

        // active() transitions to ActivatedState
        assertThat(suspended.active(), instanceOf(ActivatedState.class));

        // code()
        assertThat(suspended.code(), is(CustomerStateType.SUSPENDED));

        // toString coverage
        assertThat(suspended.toString(), containsString("SuspendedState"));
    }

    @Test
    void suspendedState_redPath_createThrows() {
        CustomerState suspended = new SuspendedState();
        IllegalStateException ex = assertThrows(IllegalStateException.class, suspended::create);
        assertThat(ex.getMessage(), containsString("Cannot create customer in suspended state"));
    }

    @Test
    void nullCustomerState_greenPath_singletonAndNoOps() {
        NullCustomerState s1 = NullCustomerState.getInstance();
        NullCustomerState s2 = NullCustomerState.getInstance();

        // Singleton
        assertThat(s1, sameInstance(s2));

        // All operations return itself (no-op)
        assertThat(s1.create(), sameInstance(s1));
        assertThat(s1.suspend(), sameInstance(s1));
        assertThat(s1.active(), sameInstance(s1));

        // Code returns CREATE by design (acts as neutral initial state)
        assertThat(s1.code(), is(CustomerStateType.CREATE));
    }

    @Test
    void customerStateType_greenPath_toStateMapping() {
        assertThat(CustomerStateType.CREATE.toState(), instanceOf(CreatedState.class));
        assertThat(CustomerStateType.ACTIVE.toState(), instanceOf(ActivatedState.class));
        assertThat(CustomerStateType.SUSPENDED.toState(), instanceOf(SuspendedState.class));
    }
}
