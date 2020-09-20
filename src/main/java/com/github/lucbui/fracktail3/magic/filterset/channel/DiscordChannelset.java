package com.github.lucbui.fracktail3.magic.filterset.channel;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.filterset.user.PlatformSpecificUserset;
import com.github.lucbui.fracktail3.magic.platform.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.platform.discord.DiscordPlatform;
import discord4j.common.util.Snowflake;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

public class DiscordChannelset extends PlatformSpecificUserset<DiscordContext> {
    private boolean permitDms;
    private Set<Snowflake> guildSnowflakes;
    private Set<Snowflake> channelSnowflakes;

    public DiscordChannelset(String name, boolean blacklist, String extendsRoleset) {
        super(name, blacklist, extendsRoleset, DiscordPlatform.INSTANCE);
        this.guildSnowflakes = null;
        this.channelSnowflakes = null;
    }

    public DiscordChannelset(String name, Set<Snowflake> guildSnowflakes, Set<Snowflake> channelSnowflakes) {
        super(name, DiscordPlatform.INSTANCE);
        this.guildSnowflakes = guildSnowflakes;
        this.channelSnowflakes = channelSnowflakes;
    }

    public DiscordChannelset(String name, boolean blacklist, String extendsRoleset, Set<Snowflake> guildSnowflakes, Set<Snowflake> channelSnowflakes) {
        super(name, blacklist, extendsRoleset, DiscordPlatform.INSTANCE);
        this.guildSnowflakes = guildSnowflakes;
        this.channelSnowflakes = channelSnowflakes;
    }

    public boolean isPermitDms() {
        return permitDms;
    }

    public void setPermitDms(boolean permitDms) {
        this.permitDms = permitDms;
    }

    public Set<Snowflake> getGuildSnowflakes() {
        return guildSnowflakes;
    }

    public void setGuildSnowflakes(Set<Snowflake> guildSnowflakes) {
        this.guildSnowflakes = guildSnowflakes;
    }

    public Set<Snowflake> getChannelSnowflakes() {
        return channelSnowflakes;
    }

    public void setChannelSnowflakes(Set<Snowflake> channelSnowflakes) {
        this.channelSnowflakes = channelSnowflakes;
    }

    @Override
    protected Mono<Boolean> matchesForPlatform(Bot bot, DiscordContext context) {
        if(CollectionUtils.isEmpty(guildSnowflakes) && CollectionUtils.isEmpty(channelSnowflakes)) {
            return Mono.just(true);
        }

        return Mono.just(isLegalGuild(context) && isLegalChannel(context));
    }

    private boolean isLegalGuild(DiscordContext context) {
        Optional<Snowflake> guildId = context.getEvent().getGuildId();
        return CollectionUtils.isEmpty(guildSnowflakes) ||
                guildId.map(snowflake -> guildSnowflakes.contains(snowflake)).orElse(permitDms);
    }

    private boolean isLegalChannel(DiscordContext context) {
        Snowflake channelId = context.getEvent().getMessage().getChannelId();
        return CollectionUtils.isEmpty(channelSnowflakes) || channelSnowflakes.contains(channelId);
    }
}
