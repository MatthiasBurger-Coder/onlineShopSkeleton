package de.burger.it.domain.order.state;

/**
 * Enum representing the possible states of an order.
 * Each state type can be converted to its corresponding state object.
 */
public enum OrderStateType {
    /**
     * Represents a newly created order.
     */
    NEW {
        @Override
        public OrderState toState() {
            return new NewState();
        }
    },
    
    /**
     * Represents a paid order.
     */
    PAID {
        @Override
        public OrderState toState() {
            return new PaidState();
        }
    },
    
    /**
     * Represents a cancelled order.
     */
    CANCELLED {
        @Override
        public OrderState toState() {
            return new CanceledState();
        }
    },
    
    /**
     * Represents a delivered order.
     */
    DELIVERED {
        @Override
        public OrderState toState() {
            return new DeliveredState();
        }
    };

    /**
     * Converts this state type to its corresponding state object.
     *
     * @return the state object corresponding to this state type
     */
    public abstract OrderState toState();
}
