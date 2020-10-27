package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.BanEvent;

import java.util.Locale;

public class BanContext extends DiscordBasePlatformContext<BanEvent> {
    public BanContext(Bot bot, DiscordPlatform platform, BanEvent payload) {
        super(bot, platform, payload);
    }

    public BanContext(Bot bot, DiscordPlatform platform, Locale locale, BanEvent payload) {
        super(bot, platform, payload);
    }
}
