package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import com.github.lucbui.fracktail3.spring.annotation.schedule.*;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.util.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
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

    @Autowired
    private Plugins plugins;

    protected ScheduleConfigurer() {
        super(Schedule.class);
    }

    @Override
    protected void handleMethod(Object obj, Method method) {
        String id = returnStringOrMemberName(method.getAnnotation(Schedule.class).value(), method);
        LOGGER.debug("Adding @Scheduled-annotated method {}", id);

        ScheduledEvent event = new ScheduledEvent(id, getTrigger(method), factory.createScheduledAction(obj, method));
        addOrMerge(event);
    }

    @Override
    protected void handleField(Object obj, Field field) {
        String id = returnStringOrMemberName(field.getAnnotation(Schedule.class).value(), field);
        LOGGER.debug("Adding @Scheduled-annotated method {}", id);

        ScheduledEvent event = new ScheduledEvent(id, getTrigger(field), factory.createScheduledAction(obj, field));
        addOrMerge(event);
    }

    private ScheduleEventTrigger getTrigger(AnnotatedElement member) {
        if(member.isAnnotationPresent(Cron.class)) {
            return AnnotationUtils.fromCron(member.getAnnotation(Cron.class));
        } else if(member.isAnnotationPresent(RunAt.class)) {
            return AnnotationUtils.fromRunAt(member.getAnnotation(RunAt.class));
        } else if(member.isAnnotationPresent(RunAfter.class)) {
            return AnnotationUtils.fromRunAfter(member.getAnnotation(RunAfter.class));
        } else if(member.isAnnotationPresent(RunEvery.class)) {
            return AnnotationUtils.fromRunEvery(member.getAnnotation(RunEvery.class));
        }
        throw new BotConfigurationException("@Scheduled must be annotated with @Cron, @RunAt, @RunAfter, or @RunEvery");
    }

    private void addOrMerge(ScheduledEvent event) {
        Optional<ScheduledEvent> old = scheduledEvents.getById(event.getId());
        if(old.isPresent()) {
            LOGGER.debug("Overwriting command, so ignoring");
            // plugins.onScheduledEventMerge(old.get(), event);
        } else {
            scheduledEvents.add(event);
            // plugins.onCommandAdd(event);
        }
    }
}
