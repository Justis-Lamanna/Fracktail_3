package com.github.lucbui.fracktail3.magic.command;

import com.github.lucbui.fracktail3.magic.util.IdStore;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A list of usable commands.
 */
public class CommandList extends IdStore<Command> {
    /**
     * Initializes this CommandList with a list of commands
     * @param commands The commands to use
     */
    public CommandList(List<Command> commands) {
        super(commands);
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
     * Get the number of commands in the list
     * @return The number of commands
     */
    public int getNumberOfCommands() {
        return size();
    }
}
