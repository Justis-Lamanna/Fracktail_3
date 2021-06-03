package com.github.lucbui.fracktail3.spring;

import com.github.lucbui.fracktail3.magic.platform.SchedulePlatform;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Scheduler springScheduler(TaskScheduler taskScheduler) {
        return new SpringScheduler(taskScheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledEvents scheduledEvents() {
        return ScheduledEvents.empty();
    }

    @Bean
    public SchedulePlatform schedulePlatform(Scheduler scheduler, ScheduledEvents events) {
        return new SchedulePlatform(scheduler, events);
    }
}
