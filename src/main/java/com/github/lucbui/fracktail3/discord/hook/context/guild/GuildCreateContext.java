package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.GuildCreateEvent;

import java.util.Locale;

public class GuildCreateContext extends DiscordBasePlatformContext<GuildCreateEvent> {
    public GuildCreateContext(Bot bot, DiscordPlatform platform, GuildCreateEvent payload) {
        super(bot, platform, payload);
    }

    public GuildCreateContext(Bot bot, DiscordPlatform platform, Locale locale, GuildCreateEvent payload) {
        super(bot, platform, payload);
    }
}
