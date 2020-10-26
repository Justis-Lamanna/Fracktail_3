package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.InviteDeleteEvent;

import java.util.Locale;

public class InviteDeleteContext extends DiscordBasePlatformContext<InviteDeleteEvent> {
    public InviteDeleteContext(Bot bot, DiscordPlatform platform, InviteDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public InviteDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, InviteDeleteEvent payload) {
        super(bot, platform, locale, payload);
    }
}
