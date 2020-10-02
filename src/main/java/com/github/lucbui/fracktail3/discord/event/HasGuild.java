package com.github.lucbui.fracktail3.discord.event;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import reactor.core.publisher.Mono;

/**
 * Wraps an event which provides a guild
 */
public interface HasGuild {
    /**
     * Get the ID of the guild associated with this event
     * @return The guild snowflake
     */
    Snowflake getGuildId();

    /**
     * Get the guild object associated with this event
     * @return The guild
     */
    Mono<Guild> getGuild();
}
