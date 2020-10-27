package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.TypingStartEvent;

import java.util.Locale;

public class TypingStartContext extends DiscordBasePlatformContext<TypingStartEvent> {
    public TypingStartContext(Bot bot, DiscordPlatform platform, TypingStartEvent payload) {
        super(bot, platform, payload);
    }

    public TypingStartContext(Bot bot, DiscordPlatform platform, Locale locale, TypingStartEvent payload) {
        super(bot, platform, payload);
    }
}
