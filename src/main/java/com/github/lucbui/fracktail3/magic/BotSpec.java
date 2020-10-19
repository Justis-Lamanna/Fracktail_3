package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.platform.Platform;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The specifications for Bot behavior
 * The intention of this spec is be completely agnostic to a platform. As such, the spec can be interpreted by
 * many different PlatformHandlers to create the corresponding bot.
 *
 * This Spec is the entry point of a bot definition.
 *
 * @see Bot
 */
public class BotSpec {
    private final Map<String, Platform> platforms;
    private final CommandList commandList;

    public BotSpec(List<Platform> platforms, CommandList commandList) {
        this.platforms = platforms.stream().collect(Collectors.toMap(Id::getId, Function.identity()));
        this.commandList = commandList;
    }

    /**
     * Get the platforms supported
     * @return The supported platforms
     */
    public Set<Platform> getPlatforms() {
        return new HashSet<>(platforms.values());
    }

    /**
     * Get a platform by its ID
     * @param id The ID of the platform
     * @return The platform, or empty if none match
     */
    public Optional<Platform> getPlatform(String id) {
        return Optional.ofNullable(platforms.get(id));
    }

    /**
     * Get the list of Commands this bot performs.
     * @return The list of Commands this bot performs.
     */
    public CommandList getCommandList() {
        return commandList;
    }
}
