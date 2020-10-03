package com.github.lucbui.fracktail3.magic.hook;

import com.github.lucbui.fracktail3.magic.config.Config;

public class EventContext<E extends SupportedEvent> {
    private final Config config;
    private final HookEvent<E, ?> event;

    public EventContext(Config config, HookEvent<E, ?> event) {
        this.config = config;
        this.event = event;
    }

    public Config getConfig() {
        return config;
    }

    public HookEvent<E, ?> getEvent() {
        return event;
    }
}
