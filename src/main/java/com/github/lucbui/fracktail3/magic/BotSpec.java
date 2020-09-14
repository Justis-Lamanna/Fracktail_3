package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.filterset.user.Usersets;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;

import javax.annotation.Nonnull;
import java.util.*;

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
    private final Map<String, Platform<?, ?, ?>> platforms = new HashMap<>();
    private final Map<String, Config> configs = new HashMap<>();
    private Usersets usersets;
    private BehaviorList behaviorList;

    public <C extends Config> void addConfig(Platform<C, ?, ?> platform, C config) {
        if(platforms.containsKey(platform.id())) {
            throw new BotConfigurationException("Configuration already exists for id " + platform.id());
        }
        platforms.put(platform.id(), platform);
        configs.put(platform.id(), config);
    }

    public <C extends Config> Optional<C> getConfig(Platform<C, ?, ?> platform) {
        return configs.containsKey(platform) ? Optional.of((C)configs.get(platform)) : Optional.empty();
    }

    public Set<Platform<?, ?, ?>> getPlatforms() {
        return new HashSet<>(platforms.values());
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
        return getUsersets().getUserset(name);
    }

    /**
     * Set the Rolesets defined by the user.
     * @param usersets The rolesets.
     * @see Usersets
     */
    public void setUsersets(@Nonnull Usersets usersets) {
        this.usersets = Objects.requireNonNull(usersets);
    }

    /**
     * Get the list of Commands this bot performs.
     * @return The list of Commands this bot performs.
     */
    public BehaviorList getBehaviorList() {
        return behaviorList;
    }

    /**
     * Set the list of Commands this bot performs.
     * @param behaviorList The list of commands this bot should perform.
     */
    public void setBehaviorList(@Nonnull BehaviorList behaviorList) {
        this.behaviorList = behaviorList;
    }

    /**
     * Validate this BotSpec for errors
     * @throws BotConfigurationException An error occurs.
     */
    public void validate() throws BotConfigurationException {
        configs.values().forEach(c -> c.validate(this));
        usersets.validate(this);
        behaviorList.validate(this);
    }
}
