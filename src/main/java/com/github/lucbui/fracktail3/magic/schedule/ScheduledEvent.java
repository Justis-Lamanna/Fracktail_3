package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;

/**
 * Encapsulates a scheduled event
 */
public class ScheduledEvent implements Id, Disableable {
    private final String id;
    private final ScheduleEventTrigger trigger;
    private final ScheduledAction action;

    private ScheduleSubscriber.Proxy proxy;
    private boolean enabled;

    public ScheduledEvent(String id, boolean enabled, ScheduleEventTrigger trigger, ScheduledAction action) {
        this.id = id;
        this.enabled = enabled;
        this.trigger = trigger;
        this.action = action;
    }

    public ScheduledEvent(String id, ScheduleEventTrigger trigger, ScheduledAction action) {
        this(id, true, trigger, action);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Set the proxy to be used for cancelling
     * @param proxy The proxy which allows for cancellation
     */
    public void setProxy(ScheduleSubscriber.Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the trigger which dictates how to schedule this event
     * @return The trigger
     */
    public ScheduleEventTrigger getTrigger() {
        return trigger;
    }

    /**
     * Get the action which dictates how to enact this event
     * @return The action
     */
    public ScheduledAction getAction() {
        return action;
    }

    /**
     * Cancel this event
     */
    public void cancel() {
        proxy.cancel();
    }
}
