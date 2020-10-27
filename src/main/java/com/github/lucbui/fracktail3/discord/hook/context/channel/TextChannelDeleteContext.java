package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.TextChannelDeleteEvent;

import java.util.Locale;

public class TextChannelDeleteContext extends DiscordBasePlatformContext<TextChannelDeleteEvent> {
    public TextChannelDeleteContext(Bot bot, DiscordPlatform platform, TextChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public TextChannelDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, TextChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }
}
