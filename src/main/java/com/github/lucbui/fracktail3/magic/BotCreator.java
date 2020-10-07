package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.schedule.DefaultScheduler;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;

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
    private Scheduler scheduler = new DefaultScheduler();
    private Action orElse = Action.NOOP;

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
     * Set a default action to execute, if no commands match
     * @param action The action to perform
     * @return This creator
     */
    public BotCreator orElseDo(Action action) {
        this.orElse = action;
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
                new CommandList(commands, orElse));

        //Awareness checks
        platforms.forEach(this::callIfBotCreatorAware);

        commands.forEach(this::callIfBotCreatorAware);

        callIfBotCreatorAware(orElse);

        spec.validate();
        return new Bot(spec, scheduler);
    }

    private void callIfBotCreatorAware(Object obj) {
        BotCreatorAware.configure(obj, this);
    }
}
