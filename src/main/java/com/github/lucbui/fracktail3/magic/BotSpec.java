package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.filterset.user.Usersets;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;

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
    private final Map<String, Platform<?, ?>> platforms = new HashMap<>();
    private final Map<String, Config> configs = new HashMap<>();
    private final Usersets usersets;
    private final BehaviorList behaviorList;

    public BotSpec(Map<Platform<?, ?>, Config> configs, Usersets usersets, BehaviorList behaviorList) {
        addConfigs(configs);
        this.usersets = usersets;
        this.behaviorList = behaviorList;
    }

    private void addConfigs(Map<Platform<?, ?>, Config> c) {
        c.forEach(((platform, config) -> {
            platforms.put(platform.getId(), platform);
            configs.put(platform.getId(), config);
        }));
    }

    public <C extends Config> Optional<C> getConfig(Platform<C, ?> platform) {
        return configs.containsKey(platform.getId()) ? Optional.of((C)configs.get(platform.getId())) : Optional.empty();
    }

    public Set<Platform<?, ?>> getPlatforms() {
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
        configs.values().forEach(c -> c.validate(this));
        usersets.validate(this);
        behaviorList.validate(this);
    }
}
