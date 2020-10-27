package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;

import java.util.Locale;

public class ReconnectContext extends DiscordBasePlatformContext<ReconnectEvent> {
    public ReconnectContext(Bot bot, DiscordPlatform platform, ReconnectEvent payload) {
        super(bot, platform, payload);
    }

    public ReconnectContext(Bot bot, DiscordPlatform platform, Locale locale, ReconnectEvent payload) {
        super(bot, platform, payload);
    }
}
