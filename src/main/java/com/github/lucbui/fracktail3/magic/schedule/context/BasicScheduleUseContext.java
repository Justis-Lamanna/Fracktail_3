package com.github.lucbui.fracktail3.magic.schedule.context;

import com.github.lucbui.fracktail3.magic.Bot;
import lombok.Data;

import java.time.Instant;

@Data
public class BasicScheduleUseContext implements ScheduleUseContext {
    private final Bot bot;
    private final Instant triggerTime;
}
