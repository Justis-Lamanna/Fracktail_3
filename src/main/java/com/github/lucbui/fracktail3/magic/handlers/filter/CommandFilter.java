package com.github.lucbui.fracktail3.magic.handlers.filter;

public class CommandFilter extends BaseFilter {
    public static final CommandFilter DEFAULT = new CommandFilter(true);

    public CommandFilter(boolean enabled) {
        super(enabled);
    }
}
