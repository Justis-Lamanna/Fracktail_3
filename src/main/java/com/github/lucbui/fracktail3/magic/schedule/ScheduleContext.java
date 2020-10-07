package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.time.Instant;

/**
 * Context for a scheduled command
 */
public abstract class ScheduleContext {
    private final Config config;
    private final ScheduledEvent event;
    private final Instant triggerTime;

    /**
     * Initialize this context
     * @param config The configuration of the triggered command's platform
     * @param event The triggered event
     * @param triggerTime The time this context was triggered
     */
    public ScheduleContext(Config config, ScheduledEvent event, Instant triggerTime) {
        this.config = config;
        this.event = event;
        this.triggerTime = triggerTime;
    }

    /**
     * Get the config of the triggered command's platform
     * @return The triggered command's platform
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Get the instant this command was triggered
     * @return The trigger time
     */
    public Instant getTriggerTime() {
        return triggerTime;
    }

    /**
     * Get the triggered action
     * @return The triggered action
     */
    public ScheduledEvent getScheduledEvent() {
        return event;
    }
}
