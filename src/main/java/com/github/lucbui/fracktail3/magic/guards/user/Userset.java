package com.github.lucbui.fracktail3.magic.guards.user;

import com.github.lucbui.fracktail3.magic.guards.AbstractIdGuard;
import com.github.lucbui.fracktail3.magic.guards.Guard;

/**
 * Defines a named set of users
 */
public abstract class Userset extends AbstractIdGuard {
    /**
     * Default constructor for Userset
     * @param name The name of the set.
     */
    public Userset(String name) {
        super(name);
    }

    /**
     * Create a Userset filter by reference.
     * The Userset will be retrieved and checked at runtime, rather than statically.
     * @return A filter which resolves this Userset at runtime
     */
    public Guard byId() {
        return new InUsersetGuard(getId());
    }
}
