package de.burger.it.domain.cart.state;

/**
 * Enum representing the possible states of a cart.
 * Each state type can be converted to its corresponding state object.
 */
public enum CartStateType {
    /**
     * Represents a newly created cart.
     */
    CREATED {
        @Override
        public CartState toState() {
            return new CreatedCartState();
        }
    },
    
    /**
     * Represents an active cart.
     */
    ACTIVE {
        @Override
        public CartState toState() {
            return new ActiveCartState();
        }
    },
    
    /**
     * Represents an ordered cart.
     */
    ORDERED {
        @Override
        public CartState toState() {
            return new OrderedCartState();
        }
    },
    
    /**
     * Represents a closed cart.
     */
    CLOSED {
        @Override
        public CartState toState() {
            return new ClosedCartState();
        }
    };

    /**
     * Converts this state type to its corresponding state object.
     *
     * @return the state object corresponding to this state type
     */
    public abstract CartState toState();
}
