package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.filterset.AbstractIdFilter;
import com.github.lucbui.fracktail3.magic.filterset.Filter;

/**
 * Defines a named set of users
 */
public abstract class Userset extends AbstractIdFilter {
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
    public Filter byId() {
        return new InUsersetFilter(getId());
    }
}
