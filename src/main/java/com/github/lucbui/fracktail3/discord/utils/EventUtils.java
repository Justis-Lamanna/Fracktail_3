package com.github.lucbui.fracktail3.discord.utils;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.Event;

import java.util.Optional;
import java.util.Set;

/**
 * Utils related to extracting values from events
 */
public class EventUtils {
    /**
     * Get one or more user snowflakes, if the event contains them
     * @param event The event
     * @return A set of user snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getUserSnowflake(Event event) {
        return Optional.empty();
    }

    /**
     * Get one or more role snowflakes, if the event contains them
     * @param event The event
     * @return A set of role snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getRoleSnowflake(Event event) {
        return Optional.empty();
    }

    /**
     * Get one or more channel snowflakes, if the event contains them
     * @param event The event
     * @return A set of channel snowflakes related to that event. Empty if none are related.
     */
    public static Optional<Set<Snowflake>> getChannelSnowflakes(Event event) {
        return Optional.empty();
    }
}
