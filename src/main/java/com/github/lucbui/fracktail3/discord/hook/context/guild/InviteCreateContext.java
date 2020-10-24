package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.InviteCreateEvent;

import java.util.Locale;

public class InviteCreateContext extends DiscordBasePlatformContext<InviteCreateEvent> {
    public InviteCreateContext(Bot bot, DiscordPlatform platform, InviteCreateEvent payload) {
        super(bot, platform, payload);
    }

    public InviteCreateContext(Bot bot, DiscordPlatform platform, Locale locale, InviteCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
