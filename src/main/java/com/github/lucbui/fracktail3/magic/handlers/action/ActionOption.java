package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Validated;
import com.github.lucbui.fracktail3.magic.handlers.filter.ActionFilter;

public class ActionOption implements Validated {
    private ActionFilter filter;
    private Action action;

    public ActionOption(ActionFilter filter, Action action) {
        this.filter = filter;
        this.action = action;
    }

    public ActionFilter getFilter() {
        return filter;
    }

    public void setFilter(ActionFilter filter) {
        this.filter = filter;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        filter.validate(botSpec);
        action.validate(botSpec);
    }
}
