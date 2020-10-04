package com.github.lucbui.fracktail3.magic.hook;

public interface HookEvent<EVENT> {
    /**
     * Get the original event
     * @return The original event
     */
    EVENT getRawEvent();
}
