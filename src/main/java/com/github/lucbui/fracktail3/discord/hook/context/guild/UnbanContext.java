package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.UnbanEvent;

import java.util.Locale;

public class UnbanContext extends DiscordBasePlatformContext<UnbanEvent> {
    public UnbanContext(Bot bot, DiscordPlatform platform, UnbanEvent payload) {
        super(bot, platform, payload);
    }

    public UnbanContext(Bot bot, DiscordPlatform platform, Locale locale, UnbanEvent payload) {
        super(bot, platform, locale, payload);
    }
}
