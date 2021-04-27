package com.github.lucbui.fracktail3.spring.schedule.model;

import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import lombok.Data;

@Data
public class MethodScheduledComponent {
    private String id;
    private ScheduleEventTrigger trigger;
}
