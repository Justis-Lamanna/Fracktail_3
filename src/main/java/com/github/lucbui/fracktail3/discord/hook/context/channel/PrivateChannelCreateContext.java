package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.PrivateChannelCreateEvent;

import java.util.Locale;

public class PrivateChannelCreateContext extends DiscordBasePlatformContext<PrivateChannelCreateEvent> {
    public PrivateChannelCreateContext(Bot bot, DiscordPlatform platform, PrivateChannelCreateEvent payload) {
        super(bot, platform, payload);
    }

    public PrivateChannelCreateContext(Bot bot, DiscordPlatform platform, Locale locale, PrivateChannelCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
