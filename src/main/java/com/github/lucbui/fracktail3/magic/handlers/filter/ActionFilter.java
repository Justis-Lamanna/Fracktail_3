package com.github.lucbui.fracktail3.magic.handlers.filter;

public class ActionFilter extends BaseFilter {
    public static final ActionFilter DEFAULT = new ActionFilter(true);

    public ActionFilter(boolean enabled) {
        super(enabled);
    }
}
