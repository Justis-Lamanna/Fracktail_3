package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.NewsChannelUpdateEvent;

import java.util.Locale;

public class NewsChannelUpdateContext extends DiscordBasePlatformContext<NewsChannelUpdateEvent> {
    public NewsChannelUpdateContext(Bot bot, DiscordPlatform platform, NewsChannelUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public NewsChannelUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, NewsChannelUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
