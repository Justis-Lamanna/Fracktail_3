package com.github.lucbui.fracktail3.magic.schedule.trigger;

import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.reactivestreams.Publisher;

import java.time.Duration;
import java.time.Instant;

/**
 * A trigger which executes every so often
 */
public class ExecuteRepeatedlyTrigger implements ScheduleEventTrigger {
    private final Duration duration;

    /**
     * Initialize this trigger with a time between ticks
     * @param duration The amount of time between ticks
     */
    public ExecuteRepeatedlyTrigger(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Publisher<Instant> schedule(Scheduler scheduler) {
        return scheduler.every(duration);
    }

    /**
     * Get the amount of time to wait before executing
     * @return The amount of time to wait before executing
     */
    public Duration getDuration() {
        return duration;
    }
}
