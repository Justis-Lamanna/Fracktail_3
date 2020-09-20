package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A list of usable commands.
 */
public class CommandList implements Validated {
    private final List<Command> commands;
    private final Action orElse;

    /**
     * Initializes this CommandList with a list of commands
     * @param commands The commands to use
     * @param orElse An action to execute if no commands match
     */
    public CommandList(List<Command> commands, Action orElse) {
        this.commands = Collections.unmodifiableList(commands);
        this.orElse = Objects.requireNonNull(orElse);
    }

    /**
     * Get all commands
     * This list is unmodifiable, by the way
     * @return The list of commands
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Retrieve a command by its ID
     * @param id The ID to search
     * @return The command, if it exists
     */
    public Optional<Command> getCommandById(String id) {
        return commands.stream()
                .filter(c -> StringUtils.equals(id, c.getId()))
                .findFirst();
    }

    /**
     * Retrieve a mapping between ID and command
     * @return The ID-Command map
     */
    public Map<String, Command> getCommandsById() {
        return commands.stream()
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
        commands.forEach(c -> c.validate(botSpec));
        if(orElse instanceof Validated) {
            ((Validated) orElse).validate(botSpec);
        }
    }

    /**
     * Get the number of commands in the list
     * @return The number of commands
     */
    public int getNumberOfCommands() {
        return commands.size();
    }
}
