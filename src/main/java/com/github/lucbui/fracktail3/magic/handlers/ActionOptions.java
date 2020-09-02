package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;

import java.util.List;

public class ActionOptions {
    private final List<ActionOption> actions;
    private final Action _default;

    public ActionOptions(List<ActionOption> actions, Action _default) {
        this.actions = actions;
        this._default = _default;
    }

    public List<ActionOption> getActions() {
        return actions;
    }

    public Action getDefault() {
        return _default;
    }
}
