package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.filter.ActionFilter;

public class ActionOption {
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
}
