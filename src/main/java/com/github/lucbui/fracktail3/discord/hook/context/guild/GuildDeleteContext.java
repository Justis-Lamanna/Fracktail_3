package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.GuildDeleteEvent;

import java.util.Locale;

public class GuildDeleteContext extends DiscordBasePlatformContext<GuildDeleteEvent> {
    public GuildDeleteContext(Bot bot, DiscordPlatform platform, GuildDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public GuildDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, GuildDeleteEvent payload) {
        super(bot, platform, locale, payload);
    }
}
