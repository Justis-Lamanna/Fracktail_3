package com.github.lucbui.fracktail3.discord.hook.context.role;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.role.RoleDeleteEvent;

import java.util.Locale;

public class RoleDeleteContext extends DiscordBasePlatformContext<RoleDeleteEvent> {
    public RoleDeleteContext(Bot bot, DiscordPlatform platform, RoleDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public RoleDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, RoleDeleteEvent payload) {
        super(bot, platform, locale, payload);
    }
}
