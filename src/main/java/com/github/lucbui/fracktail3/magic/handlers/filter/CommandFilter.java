package com.github.lucbui.fracktail3.magic.handlers.filter;

import com.github.lucbui.fracktail3.magic.filterset.FilterSetValidator;

public class CommandFilter extends BaseFilter {
    public static final CommandFilter DEFAULT = new CommandFilter(true);

    public CommandFilter(boolean enabled) {
        super(enabled);
    }

    public CommandFilter(boolean enabled, FilterSetValidator setValidator) {
        super(enabled, setValidator);
    }
}
