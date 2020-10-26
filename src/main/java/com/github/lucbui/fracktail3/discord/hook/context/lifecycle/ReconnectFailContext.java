package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.ReconnectFailEvent;

import java.util.Locale;

public class ReconnectFailContext extends DiscordBasePlatformContext<ReconnectFailEvent> {
    public ReconnectFailContext(Bot bot, DiscordPlatform platform, ReconnectFailEvent payload) {
        super(bot, platform, payload);
    }

    public ReconnectFailContext(Bot bot, DiscordPlatform platform, Locale locale, ReconnectFailEvent payload) {
        super(bot, platform, locale, payload);
    }
}
