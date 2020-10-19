package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;

import java.time.Instant;

public interface ScheduledUseContext extends BaseContext<Instant> {
    ScheduledEvent getScheduledEvent();
}
