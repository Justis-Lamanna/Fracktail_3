package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.channel.Channelset;
import com.github.lucbui.fracktail3.magic.filterset.channel.Channelsets;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.filterset.user.Usersets;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
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
    private final Map<String, Platform<?>> platforms;
    private final Channelsets channelsets;
    private final Usersets usersets;
    private final BehaviorList behaviorList;

    public BotSpec(List<Platform<?>> platforms, Channelsets channelsets, Usersets usersets, BehaviorList behaviorList) {
        this.platforms = platforms.stream().collect(Collectors.toMap(Id::getId, Function.identity()));
        this.channelsets = channelsets;
        this.usersets = usersets;
        this.behaviorList = behaviorList;
    }

    /**
     * Get the platforms supported
     * @return The supported platforms
     */
    public Set<Platform<?>> getPlatforms() {
        return new HashSet<>(platforms.values());
    }

    /**
     * Get a platform by its ID
     * @param id The ID of the platform
     * @return The platform, or empty if none match
     */
    public Optional<Platform<?>> getPlatform(String id) {
        return Optional.ofNullable(platforms.get(id));
    }

    /**
     * Get the Channelsets defined by the user.
     * @return The channelsets.
     */
    public Channelsets getChannelsets() {
        return channelsets;
    }

    /**
     * Get a specific Channelset, by name
     * @param name The name to search
     * @return The found Channelset, or empty if none.
     */
    public Optional<Channelset> getChannelset(String name) {
        return getChannelsets().getById(name);
    }

    /**
     * Get the Rolesets defined by the user.
     * @return The rolesets.
     * @see Usersets
     */
    public Usersets getUsersets() {
        return usersets;
    }

    /**
     * Get a specific Roleset, by name.
     * If no Roleset of that name exists, an empty Optional is returned.
     * @param name The name to search
     * @return The found Roleset, or empty if none.
     * @see Userset
     */
    public Optional<Userset> getUserset(String name) {
        return getUsersets().getById(name);
    }

    /**
     * Get the list of Commands this bot performs.
     * @return The list of Commands this bot performs.
     */
    public BehaviorList getBehaviorList() {
        return behaviorList;
    }

    /**
     * Validate this BotSpec for errors
     * @throws BotConfigurationException An error occurs.
     */
    public void validate() throws BotConfigurationException {
        Validated.validate(usersets, this);
        Validated.validate(channelsets, this);
        behaviorList.validate(this);
    }
}
