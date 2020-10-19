package com.github.lucbui.fracktail3.magic.guard.channel;

import com.github.lucbui.fracktail3.magic.guard.AbstractIdGuard;
import com.github.lucbui.fracktail3.magic.guard.Guard;

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
