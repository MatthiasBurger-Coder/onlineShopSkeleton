package de.burger.it.domain.customer.state;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class CreatedState implements CustomerState{
    @Override
    public CustomerState create() {
        return this;
    }

    @Override
    public CustomerState suspend() {
        throw new IllegalStateException("Cannot suspend created customer");
    }

    @Override
    public CustomerState active() {
        return new ActivetedState();
    }

    @Override
    public CustomerStateType code() {
        return CustomerStateType.CREATE;
    }
}
