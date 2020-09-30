package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.guards.AbstractIdGuard;
import com.github.lucbui.fracktail3.magic.guards.Guard;

/**
 * Defines a named set of channels
 */
public abstract class Channelset extends AbstractIdGuard {
    /**
     * Default constructor for Channelset, not using any negation or extension
     * @param name The name of the set.
     */
    public Channelset(String name) {
        super(name);
    }

    /**
     * Create a Channelset filter by reference.
     * The Channelset will be retrieved and checked at runtime, rather than statically.
     * @return A filter which resolves this Channelset at runtime
     */
    public Guard byId() {
        return new InChannelsetGuard(getId());
    }
}
