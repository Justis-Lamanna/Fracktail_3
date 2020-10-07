package com.github.lucbui.fracktail3.magic.schedule;

import org.reactivestreams.Publisher;

import java.time.Instant;
import java.util.TimeZone;

/**
 * A trigger which schedules on a cron expressions
 */
public class CronTrigger implements ScheduleEventTrigger {
    private final String cron;
    private final TimeZone timeZone;

    /**
     * Initialize this trigger with expression and timezone
     * @param cron The Cron expression
     * @param timeZone The timezone to use
     */
    public CronTrigger(String cron, TimeZone timeZone) {
        this.cron = cron;
        this.timeZone = timeZone;
    }

    /**
     * Initialize this trigger with expression and system default timezone
     * @param cron The Cron expression
     */
    public CronTrigger(String cron) {
        this.cron = cron;
        this.timeZone = TimeZone.getDefault();
    }

    @Override
    public Publisher<Instant> schedule(Scheduler scheduler) {
        return scheduler.cron(this.cron, this.timeZone);
    }
}
