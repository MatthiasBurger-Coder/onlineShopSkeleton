package de.burger.it.domain.order.state;

public enum OrderStateType {
    NEW {
        @Override
        public OrderState toState() {
            return new NewState();
        }
    },
    PAID {
        @Override
        public OrderState toState() {
            return new PaidState();
        }
    },
    CANCELLED {
        @Override
        public OrderState toState() {
            return new CanceledState();
        }
    },
    DELIVERED {
        @Override
        public OrderState toState() {
            return new DelieveredState();
        }
    };

    public abstract OrderState toState();

}
