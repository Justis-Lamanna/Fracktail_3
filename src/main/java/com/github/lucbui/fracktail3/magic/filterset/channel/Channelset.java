package com.github.lucbui.fracktail3.magic.filterset.channel;

import com.github.lucbui.fracktail3.magic.filterset.AbstractIdFilter;
import com.github.lucbui.fracktail3.magic.filterset.Filter;

public abstract class Channelset extends AbstractIdFilter{
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
    public Filter byId() {
        return new InChannelsetFilter(getId());
    }
}
