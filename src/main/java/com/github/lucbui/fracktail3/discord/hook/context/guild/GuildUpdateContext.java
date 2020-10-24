package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.GuildUpdateEvent;

import java.util.Locale;

public class GuildUpdateContext extends DiscordBasePlatformContext<GuildUpdateEvent> {
    public GuildUpdateContext(Bot bot, DiscordPlatform platform, GuildUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public GuildUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, GuildUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
