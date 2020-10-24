package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.IntegrationsUpdateEvent;

import java.util.Locale;

public class IntegrationsUpdateContext extends DiscordBasePlatformContext<IntegrationsUpdateEvent> {
    public IntegrationsUpdateContext(Bot bot, DiscordPlatform platform, IntegrationsUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public IntegrationsUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, IntegrationsUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
