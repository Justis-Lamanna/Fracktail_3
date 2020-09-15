package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.handlers.Validated;

public class ActionOption implements Validated {
    private Filter filter;
    private Action action;

    public ActionOption(Filter filter, Action action) {
        this.filter = filter;
        this.action = action;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
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
        if(filter instanceof Validated) {
            ((Validated) filter).validate(botSpec);
        }
        action.validate(botSpec);
    }
}
