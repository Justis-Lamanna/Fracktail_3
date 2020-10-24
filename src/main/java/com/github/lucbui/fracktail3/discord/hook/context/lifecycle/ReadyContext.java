package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

import java.util.Locale;

public class ReadyContext extends DiscordBasePlatformContext<ReadyEvent> {
    public ReadyContext(Bot bot, DiscordPlatform platform, ReadyEvent payload) {
        super(bot, platform, payload);
    }

    public ReadyContext(Bot bot, DiscordPlatform platform, Locale locale, ReadyEvent payload) {
        super(bot, platform, locale, payload);
    }
}
