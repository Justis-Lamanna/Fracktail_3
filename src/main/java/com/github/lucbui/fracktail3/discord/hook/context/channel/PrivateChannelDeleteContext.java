package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.PrivateChannelDeleteEvent;

import java.util.Locale;

public class PrivateChannelDeleteContext extends DiscordBasePlatformContext<PrivateChannelDeleteEvent> {
    public PrivateChannelDeleteContext(Bot bot, DiscordPlatform platform, PrivateChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public PrivateChannelDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, PrivateChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }
}
