package com.github.lucbui.fracktail3.spring.configurer;

import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.spring.command.factory.ReflectiveCommandActionFactory;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class ScheduleConfigurer extends FieldAndMethodBasedConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleConfigurer.class);

    @Autowired
    private ReflectiveCommandActionFactory factory;

    @Autowired
    private ScheduledEvents scheduledEvents;

    protected ScheduleConfigurer() {
        super(Schedule.class);
    }

    @Override
    protected void handleMethod(Object obj, Method method) {
        addOrMerge(factory.createScheduledAction(obj, method));
    }

    @Override
    protected void handleField(Object obj, Field field) {
        addOrMerge(factory.createScheduledAction(obj, field));
    }

    private void addOrMerge(ScheduledEvent event) {
        Optional<ScheduledEvent> old = scheduledEvents.getById(event.getId());
        if(old.isPresent()) {
            LOGGER.debug("+-Overwriting scheduled event.");
            scheduledEvents.replace(event);
        } else {
            scheduledEvents.add(event);
        }
    }
}
