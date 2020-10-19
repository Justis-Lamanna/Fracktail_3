package com.github.lucbui.fracktail3.magic.schedule.trigger;

import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.reactivestreams.Publisher;

import java.time.Instant;

/**
 * Describes how to schedule an event with the scheduler
 */
public interface ScheduleEventTrigger {
    Publisher<Instant> schedule(Scheduler scheduler);
}
