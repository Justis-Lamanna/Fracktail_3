package com.github.lucbui.fracktail3.magic.handlers.filter;

public class BaseFilter {
    public static final BaseFilter DEFAULT = new BaseFilter(true);

    private boolean enabled;

    public BaseFilter(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
