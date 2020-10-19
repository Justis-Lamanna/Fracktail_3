package com.github.lucbui.fracktail3.magic.schedule.trigger;

import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.time.Instant;

/**
 * A trigger which fires after waiting some time first
 */
public class ExecuteAfterDurationTrigger implements ScheduleEventTrigger {
    private final Duration duration;

    /**
     * Initialize this trigger
     * @param duration The amount of time to wait before firing
     */
    public ExecuteAfterDurationTrigger(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Publisher<Instant> schedule(Scheduler scheduler) {
        return scheduler.wait(duration);
    }
}
