package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.schedule.action.ScheduledAction;
import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import reactor.core.publisher.Mono;

/**
 * Encapsulates a scheduled event
 */
public class ScheduledEvent implements Id {
    private final String id;
    private final ScheduleEventTrigger trigger;
    private final ScheduledAction action;

    private ScheduleSubscriber.Proxy proxy;
    private TriggerState triggerState;
    private boolean enabled;

    public ScheduledEvent(String id, boolean enabled, ScheduleEventTrigger trigger, ScheduledAction action) {
        this.id = id;
        this.enabled = enabled;
        this.trigger = trigger;
        this.action = action;
        this.triggerState = TriggerState.CREATED;
    }

    public ScheduledEvent(String id, ScheduleEventTrigger trigger, ScheduledAction action) {
        this(id, true, trigger, action);
    }

    @Override
    public String getId() {
        return this.id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
     * Mark this action as scheduled
     * @param proxy The proxy which allows for cancellation
     */
    void schedule(ScheduleSubscriber.Proxy proxy) {
        this.triggerState = TriggerState.SCHEDULED;
        this.proxy = proxy;
    }

    /**
     * Execute the action of this event
     * @param context The execution context
     * @return Asynchronous indication of completion
     */
    public Mono<Void> execute(ScheduleUseContext context) {
        triggerState = TriggerState.RUNNING;
        return action.execute(context);
    }

    /**
     * Mark this action as completed
     */
    void complete() {
        if(triggerState != TriggerState.CANCELLED) {
            this.triggerState = TriggerState.COMPLETED;
        }
        this.proxy = null;
    }

    /**
     * Cancel this event
     */
    public void cancel() {
        triggerState = TriggerState.CANCELLED;
        if(proxy != null) {
            proxy.cancel();
        }
    }

    /**
     * Get the state of this event
     * @return One of the states this event can be in
     */
    public TriggerState getState() {
        return enabled ? triggerState : TriggerState.DISABLED;
    }
}
