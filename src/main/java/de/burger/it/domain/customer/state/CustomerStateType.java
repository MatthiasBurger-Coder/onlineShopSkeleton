package de.burger.it.domain.customer.state;

/**
 * Enum representing the possible states of a customer.
 * Each state type can be converted to its corresponding state object.
 */
public enum CustomerStateType {
    /**
     * Represents a newly created customer.
     */
    CREATE {
        @Override
        public CustomerState toState() { 
            return new CreatedState(); 
        }
    },
    
    /**
     * Represents an active customer.
     */
    ACTIVE {
        @Override
        public CustomerState toState() { 
            return new ActivatedState(); 
        }
    },
    
    /**
     * Represents a suspended customer.
     */
    SUSPENDED {
        @Override
        public CustomerState toState() { 
            return new SuspendedState(); 
        }
    };

    /**
     * Converts this state type to its corresponding state object.
     *
     * @return the state object corresponding to this state type
     */
    public abstract CustomerState toState();
}
