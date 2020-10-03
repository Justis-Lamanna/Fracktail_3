package com.github.lucbui.fracktail3.magic.hook;

public interface HookEvent<TYPE extends SupportedEvent, EVENT> {
    /**
     * Get the type of event
     * @return The event being returned
     */
    TYPE getEventType();

    /**
     * Get the original event
     * @return The original event
     */
    EVENT getRawEvent();
}
