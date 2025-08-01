package de.burger.it.domain.cart.state;

public enum CartStateType {
    CREATED {
        public CartState toState() {
            return new CreatedCartState();
        }
    },
    ACTIVE {
        public CartState toState() {
            return new ActiveCartState();
        }
    },
    ORDERED {
        public CartState toState() {
            return new OrderedCartState();
        }
    },
    CLOSED {
        public CartState toState() {
            return new ClosedCartState();
        }
    };

    public abstract CartState toState();
}
