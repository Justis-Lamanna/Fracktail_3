package com.github.milomarten.fracktail3.magic.schedule;

/**
 * One of the states the event can be in
 */
public enum TriggerState {
    /**
     * Event was created, but has not been scheduled
     */
    CREATED,

    /**
     * Event has been scheduled, but hasn't run yet
     */
    SCHEDULED,

    /**
     * Event has run at least once
     */
    RUNNING,

    /**
     * Event has completed running
     */
    COMPLETED,

    /**
     * Event was cancelled
     */
    CANCELLED,

    /**
     * Event is disabled, and will not fire
     */
    DISABLED
}
