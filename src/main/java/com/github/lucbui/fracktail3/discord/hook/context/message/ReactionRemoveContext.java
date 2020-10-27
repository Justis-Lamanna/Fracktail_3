package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.ReactionRemoveEvent;

import java.util.Locale;

public class ReactionRemoveContext extends DiscordBasePlatformContext<ReactionRemoveEvent> {
    public ReactionRemoveContext(Bot bot, DiscordPlatform platform, ReactionRemoveEvent payload) {
        super(bot, platform, payload);
    }

    public ReactionRemoveContext(Bot bot, DiscordPlatform platform, Locale locale, ReactionRemoveEvent payload) {
        super(bot, platform, payload);
    }
}
