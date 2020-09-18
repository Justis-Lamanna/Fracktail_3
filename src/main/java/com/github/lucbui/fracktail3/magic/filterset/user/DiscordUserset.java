package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordPlatform;
import discord4j.common.util.Snowflake;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

/**
 * Discord-specific userset, which can filter on some combination of users or roles
 */
public class DiscordUserset extends PlatformSpecificUserset<DiscordContext> {
    private Set<Snowflake> userSnowflakes;
    private Set<Snowflake> roleSnowflakes;

    /**
     * Initialize a Userset with a name, negation, and extends
     * @param name The name of the Userset
     * @param blacklist Whether the Userset is negated or not
     * @param extendsRoleset If non-null, the Userset extended by this one
     */
    public DiscordUserset(String name, boolean blacklist, String extendsRoleset) {
        super(name, blacklist, extendsRoleset, DiscordPlatform.INSTANCE);
        this.userSnowflakes = null;
        this.roleSnowflakes = null;
    }

    /**
     * Initialize a Userset with a name, and snowflakes.
     * This Userset is not negated, and extends no other Userset
     * @param name The name of the Userset
     * @param userSnowflakes The list of snowflakes that correspond to User IDs
     * @param roleSnowflakes The list of snowflakes that correspond to Role IDs
     */
    public DiscordUserset(String name, Set<Snowflake> userSnowflakes, Set<Snowflake> roleSnowflakes) {
        super(name, DiscordPlatform.INSTANCE);
        this.userSnowflakes = userSnowflakes;
        this.roleSnowflakes = roleSnowflakes;
    }

    /**
     * Initialize a Userset with a name, negation, and extends
     * @param name The name of the Userset
     * @param blacklist Whether the Userset is negated or not
     * @param extendsRoleset If non-null, the Userset extended by this one
     * @param userSnowflakes The list of snowflakes that correspond to User IDs
     * @param roleSnowflakes The list of snowflakes that correspond to Role IDs
     */
    public DiscordUserset(String name, boolean blacklist, String extendsRoleset, Set<Snowflake> userSnowflakes, Set<Snowflake> roleSnowflakes) {
        super(name, blacklist, extendsRoleset, DiscordPlatform.INSTANCE);
        this.userSnowflakes = userSnowflakes;
        this.roleSnowflakes = roleSnowflakes;
    }

    /**
     * Factory method to create a userset for one user
     * @param name The name of the userset
     * @param user The user to allow
     * @return The created userset.
     */
    public static DiscordUserset forUser(String name, Snowflake user) {
        return new DiscordUserset(name, Collections.singleton(user), Collections.emptySet());
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
     * Get the list of valid roles.
     * If this is null, all roles are valid.
     * @return The set of role snowflakes
     */
    public Set<Snowflake> getRoleSnowflakes() {
        return roleSnowflakes;
    }

    public void setUserSnowflakes(Set<Snowflake> userSnowflakes) {
        this.userSnowflakes = userSnowflakes;
    }

    public void setRoleSnowflakes(Set<Snowflake> roleSnowflakes) {
        this.roleSnowflakes = roleSnowflakes;
    }

    private boolean isLegalUserId(Snowflake userId) {
        return CollectionUtils.isEmpty(userSnowflakes) || userSnowflakes.contains(userId);
    }

    private boolean containsLegalRole(Set<Snowflake> roles) {
        return CollectionUtils.isEmpty(roleSnowflakes) || roles.containsAll(roleSnowflakes);
    }

    @Override
    protected Mono<Boolean> matchesForPlatform(Bot bot, DiscordContext ctx) {
        if(CollectionUtils.isEmpty(userSnowflakes) && CollectionUtils.isEmpty(roleSnowflakes)) {
            return Mono.just(true);
        }

        if(ctx.isDm()) {
            return Mono.justOrEmpty(ctx.getEvent().getMessage().getAuthor())
                    .map(user -> isLegalUserId(user.getId()) && CollectionUtils.isEmpty(roleSnowflakes))
                    .defaultIfEmpty(false);
        } else {
            return Mono.justOrEmpty(ctx.getEvent().getMember())
                    .map(member -> isLegalUserId(member.getId()) && containsLegalRole(member.getRoleIds()))
                    .defaultIfEmpty(false);
        }
    }
}
