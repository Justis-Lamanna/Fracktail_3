package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.ReactionRemoveAllEvent;

import java.util.Locale;

public class ReactionRemoveAllContext extends DiscordBasePlatformContext<ReactionRemoveAllEvent> {
    public ReactionRemoveAllContext(Bot bot, DiscordPlatform platform, ReactionRemoveAllEvent payload) {
        super(bot, platform, payload);
    }

    public ReactionRemoveAllContext(Bot bot, DiscordPlatform platform, Locale locale, ReactionRemoveAllEvent payload) {
        super(bot, platform, payload);
    }
}
