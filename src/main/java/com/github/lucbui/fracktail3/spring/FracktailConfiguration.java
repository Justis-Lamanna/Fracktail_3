package com.github.lucbui.fracktail3.spring;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.schedule.DefaultScheduler;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;

@Configuration
public class FracktailConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CommandList commandList() {
        return CommandList.empty();
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledEvents scheduledEvents() {
        return ScheduledEvents.empty();
    }

    @Bean
    @ConditionalOnMissingBean
    public Plugins plugins() {
        return new Plugins();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(TaskScheduler.class)
    public Scheduler springScheduler(TaskScheduler taskScheduler) {
        return new SpringScheduler(taskScheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    public Scheduler defaultScheduler() {
        return new DefaultScheduler();
    }

    @Bean
    @ConditionalOnMissingBean
    public BotSpec botSpec(List<Platform> platforms, CommandList commandList, ScheduledEvents eventList) {
        return new BotSpec(
                platforms,
                commandList,
                eventList
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public Bot bot(BotSpec botSpec, Scheduler scheduler) {
        return new Bot(botSpec, scheduler);
    }
}
