package com.github.lucbui.fracktail3.discord.hook.context.role;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.role.RoleCreateEvent;

import java.util.Locale;

public class RoleCreateContext extends DiscordBasePlatformContext<RoleCreateEvent> {
    public RoleCreateContext(Bot bot, DiscordPlatform platform, RoleCreateEvent payload) {
        super(bot, platform, payload);
    }

    public RoleCreateContext(Bot bot, DiscordPlatform platform, Locale locale, RoleCreateEvent payload) {
        super(bot, platform, payload);
    }
}
