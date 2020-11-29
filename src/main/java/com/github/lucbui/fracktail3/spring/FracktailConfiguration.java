package com.github.lucbui.fracktail3.spring;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.schedule.DefaultScheduler;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import com.github.lucbui.fracktail3.spring.plugin.CommandPlugin;
import com.github.lucbui.fracktail3.spring.plugin.Plugin;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;
import java.util.Optional;

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
    public Plugins plugins(CommandList commandList, List<Plugin> plugins) {
        Plugins p = new Plugins(plugins);
        for(Plugin plugin : plugins) {
            LOGGER.debug("Installing Plugin {}", plugin.getId());
            if(plugin instanceof CommandPlugin) {
                List<Command> commands = ((CommandPlugin) plugin).addAdditionalCommands();
                if(LOGGER.isDebugEnabled()) {
                    commands.forEach(c -> LOGGER.debug("Adding Command Bean from plugin {} of id {}", plugin.getClass(), c.getId()));
                }
                commands.forEach(c -> addOrMerge(commandList, c, p));
            }
        }
        return p;
    }

    private void addOrMerge(CommandList commandList, Command c, Plugins plugins) {
        Optional<Command> old = commandList.getCommandById(c.getId());
        if(old.isPresent()) {
            LOGGER.debug("Overwriting command. One day, the commands will be merged, instead");
            plugins.onCommandMerge(old.get(), c);
        } else {
            commandList.add(c);
            plugins.onCommandAdd(c);
        }
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
