package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;

/**
 * One potential arm for a parameterized action
 */
public class ActionOption implements Validated {
    private final Filter filter;
    private final Action action;

    /**
     * Initialize this action with a filter
     * @param filter The filter to use
     * @param action The action to perform if the filter passes
     */
    public ActionOption(Filter filter, Action action) {
        this.filter = filter;
        this.action = action;
    }

    /**
     * Get the filter to validate this arm
     * @return The filter user
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Get the action to perform
     * @return The action
     */
    public Action getAction() {
        return action;
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        Validated.validate(filter, botSpec);
        Validated.validate(action, botSpec);
    }
}
