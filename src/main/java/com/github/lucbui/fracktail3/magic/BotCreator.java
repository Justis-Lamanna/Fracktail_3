package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.filterset.user.Usersets;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A builder which creates a Bot
 *
 * Platforms, Configs, Usersets, Commands, and Actions that implement BotCreatorAware are called at the build stage.
 */
public class BotCreator implements IBuilder<Bot> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotCreator.class);

    private final Map<Platform<?>, Config> configs = new HashMap<>();
    private final Map<String, Userset> usersets = new HashMap<>();

    private final List<Command> commands = new ArrayList<>();
    private Action orElse = Action.NOOP;

    /**
     * Add a configuration for a specific platform
     * @param platform The platform to add
     * @param config The configuration of that platform
     * @param <C> The type of the Config
     * @return This creator
     */
    public <C extends Config> BotCreator withConfig(Platform<C> platform, C config) {
        configs.put(platform, config);
        return this;
    }

    /**
     * Add a Userset to this bot
     * @param userset The userset to add
     * @return This creator
     */
    public BotCreator withUserset(Userset userset) {
        Userset old = usersets.put(userset.getId(), userset);
        if(old != null) {
            LOGGER.warn("Overwriting userset " + userset.getId());
        }
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
     * Set a default action to execute, if no commands match
     * @param action The action to perform
     * @return This creator
     */
    public BotCreator withDefaultAction(Action action) {
        this.orElse = action;
        return this;
    }

    /**
     * Builds and validates the bot.
     * @return The created bot
     * @throws BotConfigurationException Validation failed
     */
    @Override
    public Bot build() throws BotConfigurationException {
        CommandList commandList = new CommandList(commands, orElse);
        BotSpec spec = new BotSpec(
                configs,
                new Usersets(usersets),
                new BehaviorList(commandList));

        //Awareness checks
        configs.forEach((p, c) -> {
            callIfBotCreatorAware(p);
            callIfBotCreatorAware(c);
        });

        usersets.forEach((name, set) -> {
            callIfBotCreatorAware(set);
        });

        commands.forEach(this::callIfBotCreatorAware);

        callIfBotCreatorAware(orElse);

        spec.validate();
        return new Bot(spec);
    }

    private void callIfBotCreatorAware(Object obj) {
        if(obj instanceof BotCreatorAware) {
            ((BotCreatorAware) obj).configure(this);
        }
    }
}
