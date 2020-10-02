package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.platform.DiscordContext;
import com.github.lucbui.fracktail3.discord.utils.EventUtils;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.guards.user.PlatformSpecificUserset;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.Event;
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
    /**
     * Userset that matches every user
     */
    public static final DiscordUserset ALL_USERS = new DiscordUserset("discord_all", null, null);

    /**
     * Userset that matches no user
     */
    public static final DiscordUserset NO_USERS = new DiscordUserset("discord_none", Collections.emptySet(), Collections.emptySet());

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
        super(name, DiscordContext.class);
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

    @Override
    protected Mono<Boolean> matchesForPlatform(Bot bot, DiscordContext ctx) {
        return Mono.just(isLegalUserId(ctx) && isLegalRole(ctx));
    }

    private boolean isLegalUserId(DiscordContext context) {
        if(userSnowflakes == null) return true;
        Optional<Snowflake> user = context.getEvent().getMessage().getAuthor().map(User::getId);
        return user.map(userSnowflakes::contains).orElse(false);
    }

    private boolean isLegalRole(DiscordContext context) {
        if(roleSnowflakes == null) return true;
        Optional<Set<Snowflake>> roles = context.getEvent().getMember().map(Member::getRoleIds);
        return roles.map(set -> hasOverlap(set, roleSnowflakes)).orElse(false);
    }

    private static boolean hasOverlap(Set<?> one, Set<?> two) {
        return !SetUtils.intersection(one, two).isEmpty();
    }

    /**
     * Check if this userset matches for the provided event
     * @param event The event
     * @return Asynchronous true, if matches
     */
    public Mono<Boolean> matchesForEvent(Event event) {
        if(CollectionUtils.isEmpty(userSnowflakes) && CollectionUtils.isEmpty(roleSnowflakes)) {
            return Mono.just(true);
        }

        return Mono.just(isLegalUserId(event) && isLegalRole(event));
    }

    private boolean isLegalUserId(Event event) {
        if(userSnowflakes == null) return true;
        Optional<Set<Snowflake>> users = EventUtils.getUserSnowflake(event);
        return CollectionUtils.isEmpty(userSnowflakes) || users.map(set -> hasOverlap(set, userSnowflakes)).orElse(false);
    }

    private boolean isLegalRole(Event event) {
        if(roleSnowflakes == null) return true;
        Optional<Set<Snowflake>> roles = EventUtils.getRoleSnowflake(event);
        return CollectionUtils.isEmpty(roleSnowflakes) || roles.map(set -> hasOverlap(set, roleSnowflakes)).orElse(false);
    }

    /**
     * Create an Event Hook Guard for this Userset
     * @return A guard which allows usage only from a specific userset
     */
    public DiscordEventHookGuard<Event> eventForId() {
        return new EventHookByUsersetId(getId());
    }
}
