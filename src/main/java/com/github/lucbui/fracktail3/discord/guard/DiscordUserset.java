package com.github.lucbui.fracktail3.discord.guard;

import discord4j.common.util.Snowflake;

import java.util.Collections;
import java.util.Set;

/**
 * Discord-specific userset, which can filter on some combination of users or roles
 */
public class DiscordUserset {
    /**
     * Userset that matches every user.
     * Note that this userset is automatically included, if DiscordConfigurationBuilder is used.
     */
    public static final DiscordUserset ALL_USERS = new DiscordUserset(null);

    /**
     * Userset that matches no user
     * Note that this userset is automatically included, if DiscordConfigurationBuilder is used.
     */
    public static final DiscordUserset NO_USERS = new DiscordUserset(Collections.emptySet());

    private final Set<Snowflake> userSnowflakes;

    /**
     * Initialize a Userset with a name, and snowflakes.
     * This Userset is not negated, and extends no other Userset
     * If userSnowflakes or roleSnowflakes is null, this is equivalent to matching on all users or roles
     * If userSnowflakes or roleSnowflakes is empty, this is equivalent to matching on no users or roles
     * @param userSnowflakes The list of snowflakes that correspond to User IDs
     */
    public DiscordUserset(Set<Snowflake> userSnowflakes) {
        this.userSnowflakes = userSnowflakes;
    }

    /**
     * Factory method to create a userset for one user
     * @param user The user to allow
     * @return The created userset.
     */
    public static DiscordUserset forUser(Snowflake user) {
        return new DiscordUserset(Collections.singleton(user));
    }

    /**
     * Factory method to create a userset for one user
     * @param user The user to allow
     * @return The created userset.
     */
    public static DiscordUserset forUser(long user) {
        return new DiscordUserset(Collections.singleton(Snowflake.of(user)));
    }

    /**
     * Get the list of valid user snowflakes.
     * If this is null, all users are valid.
     * @return The set of user snowflakes.
     */
    public Set<Snowflake> getUserSnowflakes() {
        return userSnowflakes;
    }

    /**
     * Tests if this set matches every channel
     * @return True, if this is the universal set
     */
    public boolean matchesEveryUser() {
        return userSnowflakes == null;
    }

    /**
     * Tests if this set matches no channel
     * @return True, if this is the empty set
     */
    public boolean matchesNoUser() {
        return userSnowflakes != null && userSnowflakes.isEmpty();
    }
}
