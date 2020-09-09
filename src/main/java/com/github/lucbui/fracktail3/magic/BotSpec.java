package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.role.Userset;
import com.github.lucbui.fracktail3.magic.role.Usersets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

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
    private DiscordConfiguration discordConfig;
    private Usersets usersets;
    private BehaviorList behaviorList;

    /**
     * Get the Discord Configuration, if it exists.
     * The Discord Configuration controls configuration of use for the Discord PlatformHandler. The absence of this
     * Configuration indicates the bot should not be used for Discord.
     * @return The Discord Configuration, if it exists, or empty if none.
     * @see DiscordConfiguration
     */
    public Optional<DiscordConfiguration> getDiscordConfiguration() {
        return Optional.ofNullable(discordConfig);
    }

    /**
     * Set the Discord Configuration.
     * Setting this Configuration to null indicates the bot should not be used for Discord.
     * @param discordConfig The Discord Configuration
     * @see DiscordConfiguration
     */
    public void setDiscordConfig(@Nullable DiscordConfiguration discordConfig) {
        this.discordConfig = discordConfig;
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
        discordConfig.validate();
        usersets.validate();
        behaviorList.validate(this);
    }
}
