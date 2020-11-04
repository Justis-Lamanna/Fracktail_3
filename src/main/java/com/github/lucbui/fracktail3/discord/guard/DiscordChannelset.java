package com.github.lucbui.fracktail3.discord.guard;

import com.github.lucbui.fracktail3.magic.guard.channel.Channelset;
import discord4j.common.util.Snowflake;

import java.util.Collections;
import java.util.Set;

/**
 * Discord-specific Channelset which encapsulates a set of Discord channels
 */
public class DiscordChannelset implements Channelset {
    /**
     * Channelset that matches every channel
     */
    public static final DiscordChannelset ALL_CHANNELS = new DiscordChannelset(null);

    /**
     * Channelset that matches no channels
     */
    public static final DiscordChannelset NO_CHANNELS = new DiscordChannelset(Collections.emptySet());

    private Set<Snowflake> channelSnowflakes;

    /**
     * Create a channelset that wraps the following channels
     * If channelSnowflakes is null, this is the same as matching all channels.
     * If channelSnowflakes is empty, this is the same as matching no channels.
     * @param channelSnowflakes The set of legal channels
     */
    public DiscordChannelset(Set<Snowflake> channelSnowflakes) {
        this.channelSnowflakes = channelSnowflakes;
    }

    /**
     * Create a Channelset for a single channel
     * @param id The ID to use
     * @return The ChannelSet
     */
    public static DiscordChannelset forChannel(Snowflake id) {
        return new DiscordChannelset(Collections.singleton(id));
    }

    /**
     * Create a Channelset for a single channel
     * @param id The ID to use
     * @return The ChannelSet
     */
    public static DiscordChannelset forChannel(long id) {
        return forChannel(Snowflake.of(id));
    }

    public Set<Snowflake> getChannelSnowflakes() {
        return channelSnowflakes;
    }

    /**
     * Tests if this set matches every channel
     * @return True, if this is the universal set
     */
    public boolean matchesEveryChannel() {
        return channelSnowflakes == null;
    }

    /**
     * Tests if this set matches no channel
     * @return True, if this is the empty set
     */
    public boolean matchesNoChannel() {
        return channelSnowflakes != null && channelSnowflakes.isEmpty();
    }

    public void setChannelSnowflakes(Set<Snowflake> channelSnowflakes) {
        this.channelSnowflakes = channelSnowflakes;
    }
}
