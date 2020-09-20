package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.Id;

/**
 * A specific type of Filter which allows for extension and negation
 */
public abstract class AbstractIdFilter implements Filter, Id {
    private final String name;

    /**
     * Create the filter
     * @param name The name of the filter
     */
    public AbstractIdFilter(String name) {
        this.name = name;
    }

    /**
     * Get the name of this filter
     * @return The name of this filter
     */
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return getName();
    }
}
