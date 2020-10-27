package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.CategoryUpdateEvent;

import java.util.Locale;

public class CategoryUpdateContext extends DiscordBasePlatformContext<CategoryUpdateEvent> {
    public CategoryUpdateContext(Bot bot, DiscordPlatform platform, CategoryUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public CategoryUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, CategoryUpdateEvent payload) {
        super(bot, platform, payload);
    }
}
