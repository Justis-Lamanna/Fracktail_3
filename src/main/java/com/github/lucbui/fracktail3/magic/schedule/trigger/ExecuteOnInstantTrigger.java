package com.github.lucbui.fracktail3.magic.schedule.trigger;

import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.reactivestreams.Publisher;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * A trigger which executes once, at a specific date and time
 */
public class ExecuteOnInstantTrigger implements ScheduleEventTrigger {
    private final Instant instant;

    /**
     * Initialize this trigger with the instant to trigger on
     * @param instant The instant to trigger on
     */
    public ExecuteOnInstantTrigger(Instant instant) {
        this.instant = instant;
    }

    /**
     * Initialize this trigger with a ZonedDateTime to trigger on
     * @param time The time to trigger on
     */
    public ExecuteOnInstantTrigger(ZonedDateTime time) {
        this.instant = time.toInstant();
    }

    @Override
    public Publisher<Instant> schedule(Scheduler scheduler) {
        return scheduler.at(instant);
    }
}
