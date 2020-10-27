package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.PinsUpdateEvent;

import java.util.Locale;

public class PinsUpdateContext extends DiscordBasePlatformContext<PinsUpdateEvent> {
    public PinsUpdateContext(Bot bot, DiscordPlatform platform, PinsUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public PinsUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, PinsUpdateEvent payload) {
        super(bot, platform, payload);
    }
}
