package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.discord.DiscordContext;
import discord4j.common.util.Snowflake;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

public class DiscordChannelset extends PlatformSpecificChannelset<DiscordContext> {
    private boolean permitDms;
    private Set<Snowflake> channelSnowflakes;

    public DiscordChannelset(String name) {
        super(name, DiscordContext.class);
        this.channelSnowflakes = null;
    }

    public DiscordChannelset(String name, boolean permitDms, Set<Snowflake> channelSnowflakes) {
        super(name, DiscordContext.class);
        this.permitDms = permitDms;
        this.channelSnowflakes = channelSnowflakes;
    }

    public static DiscordChannelset forChannel(String name, Snowflake id) {
        return new DiscordChannelset(name, false, Collections.singleton(id));
    }

    public boolean isPermitDms() {
        return permitDms;
    }

    public void setPermitDms(boolean permitDms) {
        this.permitDms = permitDms;
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
        Snowflake channelId = context.getEvent().getMessage().getChannelId();
        return CollectionUtils.isEmpty(channelSnowflakes) || channelSnowflakes.contains(channelId);
    }
}
