package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.CategoryCreateEvent;

import java.util.Locale;

public class CategoryCreateContext extends DiscordBasePlatformContext<CategoryCreateEvent> {
    public CategoryCreateContext(Bot bot, DiscordPlatform platform, CategoryCreateEvent payload) {
        super(bot, platform, payload);
    }

    public CategoryCreateContext(Bot bot, DiscordPlatform platform, Locale locale, CategoryCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
