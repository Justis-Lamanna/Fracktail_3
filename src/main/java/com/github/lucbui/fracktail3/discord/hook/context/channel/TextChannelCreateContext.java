package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;

import java.util.Locale;

public class TextChannelCreateContext extends DiscordBasePlatformContext<TextChannelCreateEvent> {
    public TextChannelCreateContext(Bot bot, DiscordPlatform platform, TextChannelCreateEvent payload) {
        super(bot, platform, payload);
    }

    public TextChannelCreateContext(Bot bot, DiscordPlatform platform, Locale locale, TextChannelCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
