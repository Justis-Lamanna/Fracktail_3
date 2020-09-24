package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.discord.DiscordContext;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Discord-specific userset, which can filter on some combination of users or roles
 */
public class DiscordUserset extends PlatformSpecificUserset<DiscordContext> {
    private boolean permitDms;
    private Set<Snowflake> userSnowflakes;
    private Set<Snowflake> roleSnowflakes;

    /**
     * Initialize a Userset with a name, and snowflakes.
     * This Userset is not negated, and extends no other Userset
     * @param name The name of the Userset
     * @param userSnowflakes The list of snowflakes that correspond to User IDs
     * @param roleSnowflakes The list of snowflakes that correspond to Role IDs
     */
    public DiscordUserset(String name, boolean permitDms, Set<Snowflake> userSnowflakes, Set<Snowflake> roleSnowflakes) {
        super(name, DiscordContext.class);
        this.permitDms = permitDms;
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
        return new DiscordUserset(name, true, Collections.singleton(user), Collections.emptySet());
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

    public boolean isPermitDms() {
        return permitDms;
    }

    public void setPermitDms(boolean permitDms) {
        this.permitDms = permitDms;
    }

    @Override
    protected Mono<Boolean> matchesForPlatform(Bot bot, DiscordContext ctx) {
        if(CollectionUtils.isEmpty(userSnowflakes) && CollectionUtils.isEmpty(roleSnowflakes)) {
            return Mono.just(true);
        }

        return Mono.just(isLegalUserId(ctx) && isLegalRole(ctx));
    }

    private boolean isLegalUserId(DiscordContext context) {
        Optional<Snowflake> user = context.getEvent().getMessage().getAuthor().map(User::getId);
        return CollectionUtils.isEmpty(userSnowflakes) || user.map(snowflake -> userSnowflakes.contains(snowflake)).orElse(false);
    }

    private boolean isLegalRole(DiscordContext context) {
        Optional<Set<Snowflake>> roles = context.getEvent().getMember().map(Member::getRoleIds);
        return CollectionUtils.isEmpty(roleSnowflakes) || roles.map(set -> hasOverlap(set, roleSnowflakes)).orElse(permitDms);
    }

    private boolean hasOverlap(Set<?> one, Set<?> two) {
        return !SetUtils.intersection(one, two).isEmpty();
    }
}
