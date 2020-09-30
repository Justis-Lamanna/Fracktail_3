package com.github.lucbui.fracktail3.discord.utils;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Member;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Utils related to extracting values from events
 * Note that this is somewhat of a work in progress. Only MessageCreate, ReactionAdd, and Reaction Remove are supported.
 */
public class EventUtils {
    /**
     * Get one or more user snowflakes, if the event contains them.
     * @param event The event
     * @return A set of user snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getUserSnowflake(Event event) {
        if(event instanceof MessageCreateEvent) return ((MessageCreateEvent) event).getMessage().getAuthor().map(u -> Collections.singleton(u.getId()));
        if(event instanceof ReactionAddEvent) return wrap(((ReactionAddEvent) event).getUserId());
        if(event instanceof ReactionRemoveEvent) return wrap(((ReactionRemoveEvent) event).getUserId());
        return Optional.empty();
    }

    /**
     * Get one or more role snowflakes, if the event contains them
     * Note that ReactionRemove is not supported, since role information cannot be obtained without extra calls.
     * @param event The event
     * @return A set of role snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getRoleSnowflake(Event event) {
        if(event instanceof MessageCreateEvent) return ((MessageCreateEvent) event).getMember().map(Member::getRoleIds);
        if(event instanceof ReactionAddEvent) return ((ReactionAddEvent) event).getMember().map(Member::getRoleIds);
        return Optional.empty();
    }

    /**
     * Get one or more channel snowflakes, if the event contains them
     * @param event The event
     * @return A set of channel snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getGuildSnowflakes(Event event) {
        if(event instanceof MessageCreateEvent) return ((MessageCreateEvent) event).getGuildId().map(Collections::singleton);
        if(event instanceof ReactionAddEvent) return ((ReactionAddEvent) event).getGuildId().map(Collections::singleton);
        if(event instanceof ReactionRemoveEvent) return ((ReactionRemoveEvent) event).getGuildId().map(Collections::singleton);
        return Optional.empty();
    }

    /**
     * Get one or more channel snowflakes, if the event contains them
     * @param event The event
     * @return A set of channel snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getChannelSnowflakes(Event event) {
        if(event instanceof MessageCreateEvent) return wrap(((MessageCreateEvent) event).getMessage().getChannelId());
        if(event instanceof ReactionAddEvent) return wrap(((ReactionAddEvent) event).getChannelId());
        if(event instanceof ReactionRemoveEvent) return wrap(((ReactionRemoveEvent) event).getChannelId());
        return Optional.empty();
    }

    private static <T> Optional<Set<T>> wrap(T obj) {
        return Optional.of(Collections.singleton(obj));
    }
}
