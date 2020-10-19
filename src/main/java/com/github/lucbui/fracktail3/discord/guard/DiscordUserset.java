package com.github.lucbui.fracktail3.discord.guard;

import com.github.lucbui.fracktail3.discord.context.DiscordCommandSearchContext;
import com.github.lucbui.fracktail3.magic.guard.user.Userset;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.apache.commons.collections4.SetUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Discord-specific userset, which can filter on some combination of users or roles
 */
public class DiscordUserset extends Userset {
    /**
     * Userset that matches every user.
     * Note that this userset is automatically included, if DiscordConfigurationBuilder is used.
     */
    public static final DiscordUserset ALL_USERS = new DiscordUserset("all", null, null);

    /**
     * Userset that matches no user
     * Note that this userset is automatically included, if DiscordConfigurationBuilder is used.
     */
    public static final DiscordUserset NO_USERS = new DiscordUserset("none", Collections.emptySet(), Collections.emptySet());

    private final Set<Snowflake> userSnowflakes;
    private final Set<Snowflake> roleSnowflakes;

    /**
     * Initialize a Userset with a name, and snowflakes.
     * This Userset is not negated, and extends no other Userset
     * If userSnowflakes or roleSnowflakes is null, this is equivalent to matching on all users or roles
     * If userSnowflakes or roleSnowflakes is empty, this is equivalent to matching on no users or roles
     * @param name The name of the Userset
     * @param userSnowflakes The list of snowflakes that correspond to User IDs
     * @param roleSnowflakes The list of snowflakes that correspond to Role IDs
     */
    public DiscordUserset(String name, Set<Snowflake> userSnowflakes, Set<Snowflake> roleSnowflakes) {
        super(name);
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
        return new DiscordUserset(name, Collections.singleton(user), null);
    }

    /**
     * Factory method to create a userset for one user
     * @param name The name of the userset
     * @param user The user to allow
     * @return The created userset.
     */
    public static DiscordUserset forUser(String name, long user) {
        return new DiscordUserset(name, Collections.singleton(Snowflake.of(user)), null);
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

    private boolean isLegalUserId(DiscordCommandSearchContext context) {
        if(userSnowflakes == null) return true;
        Optional<Snowflake> user = context.getPayload().getMessage().getAuthor().map(User::getId);
        return user.map(userSnowflakes::contains).orElse(false);
    }

    private boolean isLegalRole(DiscordCommandSearchContext context) {
        if(roleSnowflakes == null) return true;
        Optional<Set<Snowflake>> roles = context.getPayload().getMember().map(Member::getRoleIds);
        return roles.map(set -> hasOverlap(set, roleSnowflakes)).orElse(false);
    }

    private static boolean hasOverlap(Set<?> one, Set<?> two) {
        return !SetUtils.intersection(one, two).isEmpty();
    }

    @Override
    public Mono<Boolean> matches(BaseContext<?> ctx) {
        if(ctx instanceof DiscordCommandSearchContext) {
            DiscordCommandSearchContext dCtx = (DiscordCommandSearchContext) ctx;
            return Mono.just(isLegalUserId(dCtx) && isLegalRole(dCtx));
        }
        return Mono.just(false);
    }
}
