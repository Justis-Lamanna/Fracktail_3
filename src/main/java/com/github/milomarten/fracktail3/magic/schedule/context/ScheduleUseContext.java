package com.github.milomarten.fracktail3.magic.schedule.context;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.schedule.ScheduledEvent;

import java.time.Instant;

public interface ScheduleUseContext {
    Bot getBot();
    Instant getTriggerTime();
    ScheduledEvent getEvent();
}
