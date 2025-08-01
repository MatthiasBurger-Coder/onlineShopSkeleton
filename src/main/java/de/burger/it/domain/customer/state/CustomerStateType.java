package de.burger.it.domain.customer.state;

public enum CustomerStateType {
    CREATE {
        public CustomerState toState() { return new CreatedState(); }
    },
    ACTIVE {
        public CustomerState toState() { return new ActivetedState(); }
    },
    SUSPENDED {
        public CustomerState toState() { return new SuspendedState(); }
    };

    public abstract CustomerState toState();
}
