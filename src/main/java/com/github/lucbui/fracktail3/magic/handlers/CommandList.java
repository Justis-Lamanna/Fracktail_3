package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.model.IdStore;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A list of usable commands.
 */
public class CommandList extends IdStore<Command> implements Validated {
    private final Action orElse;

    /**
     * Initializes this CommandList with a list of commands
     * @param commands The commands to use
     * @param orElse An action to execute if no commands match
     */
    public CommandList(List<Command> commands, Action orElse) {
        super(commands);
        this.orElse = Objects.requireNonNull(orElse);
    }

    /**
     * Get all commands
     * This list is unmodifiable, by the way
     * @return The list of commands
     */
    public List<Command> getCommands() {
        return getAll();
    }

    /**
     * Retrieve a command by its ID
     * @param id The ID to search
     * @return The command, if it exists
     */
    public Optional<Command> getCommandById(String id) {
        return getById(id);
    }

    /**
     * Retrieve a mapping between ID and command
     * @return The ID-Command map
     */
    public Map<String, Command> getCommandsById() {
        return getAll().stream()
                .collect(Collectors.toMap(Command::getId, Function.identity()));
    }

    /**
     * Get the action to execute if no other commands match
     * @return The action to execute, if no others match
     */
    public Action getOrElse() {
        return orElse;
    }

    /**
     * Execute the default action
     * @param bot The bot
     * @param ctx The context
     * @return An aysnchronous marker that the action was executed
     */
    public Mono<Void> doOrElse(Bot bot, CommandContext ctx) {
        if(orElse == null) {
            return Mono.empty();
        }
        return orElse.doAction(bot, ctx);
    }

    /**
     * Validate this CommandList by validating the internal components
     * @param botSpec The botSpec to validate against
     * @throws BotConfigurationException The bot was incorrectly configured
     */
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        getAll().forEach(c -> c.validate(botSpec));
        if(orElse instanceof Validated) {
            ((Validated) orElse).validate(botSpec);
        }
    }

    /**
     * Get the number of commands in the list
     * @return The number of commands
     */
    public int getNumberOfCommands() {
        return size();
    }
}
