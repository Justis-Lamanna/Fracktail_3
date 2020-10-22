package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.schedule.DefaultScheduler;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import com.github.lucbui.fracktail3.magic.util.IBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder which creates a Bot
 *
 * Platforms, Configs, Usersets, Commands, and Actions that implement BotCreatorAware are called at the build stage.
 */
public class BotCreator implements IBuilder<Bot> {
    private final List<Platform> platforms = new ArrayList<>();
    private final List<Command> commands = new ArrayList<>();
    private final List<ScheduledEvent> events = new ArrayList<>();
    private Scheduler scheduler = new DefaultScheduler();

    /**
     * Add a platform to this bot
     * @param platform The platform to add
     * @return This creator
     */
    public BotCreator withPlatform(Platform platform) {
        platforms.add(platform);
        return this;
    }

    /**
     * Add a platform to this bot
     * @param platform The platform to add
     * @return This creator
     */
    public BotCreator withPlatform(IBuilder<? extends Platform> platform) {
        platforms.add(platform.build());
        return this;
    }

    /**
     * Add multiple platforms to this bot
     * @param newPlatforms The platforms to add
     * @return This creator
     */
    public BotCreator withPlatform(List<Platform> newPlatforms) {
        platforms.addAll(newPlatforms);
        return this;
    }

    /**
     * Add a Command to this bot
     * @param command The command to use
     * @return This creator
     */
    public BotCreator withCommand(Command command) {
        commands.add(command);
        return this;
    }

    /**
     * Add multiple commands to this bot
     * @param newCmds The commands to add
     * @return This creator
     */
    public BotCreator withCommands(List<Command> newCmds) {
        commands.addAll(newCmds);
        return this;
    }

    /**
     * Add a Command to this bot
     * @param command A builder which will create the command
     * @return This creator
     */
    public BotCreator withCommand(IBuilder<Command> command) {
        commands.add(command.build());
        return this;
    }

    /**
     * Add a scheduled event to this bot
     * @param event An event to schedule
     * @return This creator
     */
    public BotCreator withEvent(ScheduledEvent event) {
        events.add(event);
        return this;
    }

    /**
     * Add scheduled events to this bot
     * @param newEvents Events to schedule
     * @return This creator
     */
    public BotCreator withEvents(List<ScheduledEvent> newEvents) {
        events.addAll(newEvents);
        return this;
    }

    /**
     * Add a scheduled event to this bot
     * @param event An event to schedule
     * @return This creator
     */
    public BotCreator withEvent(IBuilder<ScheduledEvent> event) {
        events.add(event.build());
        return this;
    }

    /**
     * Set the scheduler to use
     * @param scheduler The scheduler to use
     * @return This creator
     */
    public BotCreator withScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * Builds and validates the bot.
     * @return The created bot
     * @throws BotConfigurationException Validation failed
     */
    @Override
    public Bot build() throws BotConfigurationException {
        BotSpec spec = new BotSpec(
                platforms,
                new CommandList(commands), new ScheduledEvents(events));

        //Awareness checks
        platforms.forEach(this::callIfBotCreatorAware);

        commands.forEach(this::callIfBotCreatorAware);

        return new Bot(spec, scheduler);
    }

    private void callIfBotCreatorAware(Object obj) {
        BotCreatorAware.configure(obj, this);
    }
}
