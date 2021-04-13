package com.github.lucbui.fracktail3.magic.schedule.context;

import com.github.lucbui.fracktail3.magic.Bot;

import java.time.Instant;

public interface ScheduleUseContext {
    Bot getBot();
    Instant getTriggerTime();
}
