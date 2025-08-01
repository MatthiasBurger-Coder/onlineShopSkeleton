package de.burger.it.domain.customer.state;

public interface CustomerState {
    CustomerState create();
    CustomerState suspend();
    CustomerState active();
    CustomerStateType code();
}
