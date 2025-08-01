package de.burger.it.application.customer.handler;

import de.burger.it.application.dispatcher.StateEventHandler;
import de.burger.it.domain.customer.event.CustomerSuspendEvent;
import de.burger.it.domain.customer.port.CustomerStatusAssignmentPort;
import de.burger.it.domain.customer.state.CustomerStateType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OnCustomerSuspendAssignSuspend implements StateEventHandler<CustomerStateType, CustomerSuspendEvent> {

    private final CustomerStatusAssignmentPort customerStatusAssignmentAdapter;

    public OnCustomerSuspendAssignSuspend(CustomerStatusAssignmentPort customerStatusAssignmentAdapter) {
        this.customerStatusAssignmentAdapter = customerStatusAssignmentAdapter;
    }

    @Override
    public Collection<CustomerStateType> supportedStates() {
        return List.of(CustomerStateType.SUSPENDED);
    }

    @Override
    public Class<CustomerSuspendEvent> supportedEvent() {
        return CustomerSuspendEvent.class;
    }

    @Override
    public void execute(CustomerSuspendEvent event) {
        customerStatusAssignmentAdapter.assign(event.customer(), CustomerStateType.SUSPENDED);

    }
}
