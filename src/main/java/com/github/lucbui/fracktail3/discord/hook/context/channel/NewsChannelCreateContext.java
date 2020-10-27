package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.NewsChannelCreateEvent;

import java.util.Locale;

public class NewsChannelCreateContext extends DiscordBasePlatformContext<NewsChannelCreateEvent> {
    public NewsChannelCreateContext(Bot bot, DiscordPlatform platform, NewsChannelCreateEvent payload) {
        super(bot, platform, payload);
    }

    public NewsChannelCreateContext(Bot bot, DiscordPlatform platform, Locale locale, NewsChannelCreateEvent payload) {
        super(bot, platform, payload);
    }
}
