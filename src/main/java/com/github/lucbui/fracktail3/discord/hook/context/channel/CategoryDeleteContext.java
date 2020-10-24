package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.CategoryDeleteEvent;

import java.util.Locale;

public class CategoryDeleteContext extends DiscordBasePlatformContext<CategoryDeleteEvent> {
    public CategoryDeleteContext(Bot bot, DiscordPlatform platform, CategoryDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public CategoryDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, CategoryDeleteEvent payload) {
        super(bot, platform, locale, payload);
    }
}
