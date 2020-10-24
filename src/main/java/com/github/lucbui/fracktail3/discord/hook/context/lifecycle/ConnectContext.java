package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.ConnectEvent;

import java.util.Locale;

public class ConnectContext extends DiscordBasePlatformContext<ConnectEvent> {
    public ConnectContext(Bot bot, DiscordPlatform platform, ConnectEvent payload) {
        super(bot, platform, payload);
    }

    public ConnectContext(Bot bot, DiscordPlatform platform, Locale locale, ConnectEvent payload) {
        super(bot, platform, locale, payload);
    }
}
