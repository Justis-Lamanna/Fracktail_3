package com.github.lucbui.fracktail3.spring;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.BasicParameterParser;
import com.github.lucbui.fracktail3.magic.platform.context.ParameterParser;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import com.github.lucbui.fracktail3.spring.service.BasicRoleService;
import com.github.lucbui.fracktail3.spring.service.CompositeRoleService;
import com.github.lucbui.fracktail3.spring.service.PlatformSpecificRoleService;
import com.github.lucbui.fracktail3.spring.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;

@Configuration
public class FracktailConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(FracktailConfiguration.class);

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
    public Scheduler springScheduler(TaskScheduler taskScheduler) {
        return new SpringScheduler(taskScheduler);
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

    @Bean
    @ConditionalOnMissingBean
    public ParameterParser parameterParser() {
        return new BasicParameterParser();
    }

    @Bean
    public RoleService roleService(List<PlatformSpecificRoleService<?>> services) {
        if(services.isEmpty()) {
            return new BasicRoleService();
        } else {
            LOGGER.info("Using Platform Specific Role Services: {}", services);
            return new CompositeRoleService(services);
        }
    }
}
