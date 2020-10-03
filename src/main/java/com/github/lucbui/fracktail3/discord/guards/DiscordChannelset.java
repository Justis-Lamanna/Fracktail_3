package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.event.HookEvent;
import com.github.lucbui.fracktail3.discord.platform.DiscordContext;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.guards.channel.PlatformSpecificChannelset;
import discord4j.common.util.Snowflake;
import org.apache.commons.collections4.SetUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

/**
 * Discord-specific Channelset which encapsulates a set of Discord channels
 */
public class DiscordChannelset extends PlatformSpecificChannelset<DiscordContext> {
    /**
     * Channelset that matches every channel
     */
    public static final DiscordChannelset ALL_CHANNELS = new DiscordChannelset("discord_all", null);

    /**
     * Channelset that matches no channels
     */
    public static final DiscordChannelset NO_CHANNELS = new DiscordChannelset("discord_none", Collections.emptySet());

    private Set<Snowflake> channelSnowflakes;

    /**
     * Create a channelset that wraps the following channels
     * If channelSnowflakes is null, this is the same as matching all channels.
     * If channelSnowflakes is empty, this is the same as matching no channels.
     * @param name The name of the set
     * @param channelSnowflakes The set of legal channels
     */
    public DiscordChannelset(String name, Set<Snowflake> channelSnowflakes) {
        super(name, DiscordContext.class);
        this.channelSnowflakes = channelSnowflakes;
    }

    public static DiscordChannelset forChannel(String name, Snowflake id) {
        return new DiscordChannelset(name, Collections.singleton(id));
    }

    public Set<Snowflake> getChannelSnowflakes() {
        return channelSnowflakes;
    }

    public void setChannelSnowflakes(Set<Snowflake> channelSnowflakes) {
        this.channelSnowflakes = channelSnowflakes;
    }

    @Override
    protected Mono<Boolean> matchesForPlatform(Bot bot, DiscordContext context) {
        return Mono.just(isLegalChannel(context));
    }

    private boolean isLegalChannel(DiscordContext context) {
        if(channelSnowflakes == null) return true;
        Snowflake channelId = context.getEvent().getMessage().getChannelId();
        return channelSnowflakes.contains(channelId);
    }

    /**
     * Check if this channelset matches for the provided event
     * @param event The event
     * @return Asynchronous true, if matches
     */
    public Mono<Boolean> matchesForEvent(HookEvent event) {
        return Mono.just(isLegalChannel(event));
    }

    private boolean isLegalChannel(HookEvent event) {
        return channelSnowflakes == null;
    }

    private static boolean hasOverlap(Set<?> one, Set<?> two) {
        return !SetUtils.intersection(one, two).isEmpty();
    }

    /**
     * Create an Event Hook Guard for this Channelset
     * @return A guard which allows usage only from a specific channelset
     */
    public DiscordEventHookGuard eventForId() {
        return new EventHookByChannelsetId(getId());
    }
}
