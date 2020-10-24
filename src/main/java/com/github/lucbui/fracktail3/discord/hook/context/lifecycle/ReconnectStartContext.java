package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.ReconnectStartEvent;

import java.util.Locale;

public class ReconnectStartContext extends DiscordBasePlatformContext<ReconnectStartEvent> {
    public ReconnectStartContext(Bot bot, DiscordPlatform platform, ReconnectStartEvent payload) {
        super(bot, platform, payload);
    }

    public ReconnectStartContext(Bot bot, DiscordPlatform platform, Locale locale, ReconnectStartEvent payload) {
        super(bot, platform, locale, payload);
    }
}
