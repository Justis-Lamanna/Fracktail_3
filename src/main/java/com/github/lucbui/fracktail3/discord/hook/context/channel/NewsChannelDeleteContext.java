package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.NewsChannelDeleteEvent;

import java.util.Locale;

public class NewsChannelDeleteContext extends DiscordBasePlatformContext<NewsChannelDeleteEvent> {
    public NewsChannelDeleteContext(Bot bot, DiscordPlatform platform, NewsChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public NewsChannelDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, NewsChannelDeleteEvent payload) {
        super(bot, platform, locale, payload);
    }
}
