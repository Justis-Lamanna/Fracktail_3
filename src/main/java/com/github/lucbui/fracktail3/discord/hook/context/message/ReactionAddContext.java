package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.ReactionAddEvent;

import java.util.Locale;

public class ReactionAddContext extends DiscordBasePlatformContext<ReactionAddEvent> {
    public ReactionAddContext(Bot bot, DiscordPlatform platform, ReactionAddEvent payload) {
        super(bot, platform, payload);
    }

    public ReactionAddContext(Bot bot, DiscordPlatform platform, Locale locale, ReactionAddEvent payload) {
        super(bot, platform, locale, payload);
    }
}
