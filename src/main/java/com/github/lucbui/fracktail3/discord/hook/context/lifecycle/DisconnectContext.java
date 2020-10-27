package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.DisconnectEvent;

import java.util.Locale;

public class DisconnectContext extends DiscordBasePlatformContext<DisconnectEvent> {
    public DisconnectContext(Bot bot, DiscordPlatform platform, DisconnectEvent payload) {
        super(bot, platform, payload);
    }

    public DisconnectContext(Bot bot, DiscordPlatform platform, Locale locale, DisconnectEvent payload) {
        super(bot, platform, payload);
    }
}
