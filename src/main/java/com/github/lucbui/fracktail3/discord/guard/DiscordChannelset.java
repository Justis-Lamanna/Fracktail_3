package com.github.lucbui.fracktail3.discord.guard;

import com.github.lucbui.fracktail3.discord.context.DiscordCommandSearchContext;
import com.github.lucbui.fracktail3.magic.guard.channel.Channelset;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import discord4j.common.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

/**
 * Discord-specific Channelset which encapsulates a set of Discord channels
 */
public class DiscordChannelset extends Channelset {
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
        super(name);
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

    private boolean isLegalChannel(DiscordCommandSearchContext context) {
        if(channelSnowflakes == null) return true;
        Snowflake channelId = context.getPayload().getMessage().getChannelId();
        return channelSnowflakes.contains(channelId);
    }

    @Override
    public Mono<Boolean> matches(PlatformBaseContext<?> ctx) {
        if(ctx instanceof DiscordCommandSearchContext) {
            DiscordCommandSearchContext dCtx = (DiscordCommandSearchContext) ctx;
            return Mono.just(isLegalChannel(dCtx));
        }
        return Mono.just(false);
    }
}
