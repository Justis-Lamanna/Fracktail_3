package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.filterset.user.Usersets;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotCreator.class);

    private final Map<Platform<?, ?>, Config> configs = new HashMap<>();
    private final Map<String, Userset> usersets = new HashMap<>();

    private final List<Command> commands = new ArrayList<>();
    private Action orElse = Action.NOOP;

    public <C extends Config> BotCreator withConfig(Platform<C, ?> platform, C config) {
        configs.put(platform, config);
        return this;
    }

    public BotCreator withUserset(Userset userset) {
        Userset old = usersets.put(userset.getId(), userset);
        if(old != null) {
            LOGGER.warn("Overwriting userset " + userset.getId());
        }
        return this;
    }

    public BotCreator withCommand(Command command) {
        commands.add(command);
        return this;
    }

    public BotCreator withDefaultAction(Action action) {
        this.orElse = action;
        return this;
    }

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
