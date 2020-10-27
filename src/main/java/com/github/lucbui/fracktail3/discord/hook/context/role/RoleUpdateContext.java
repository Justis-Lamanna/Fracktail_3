package com.github.lucbui.fracktail3.discord.hook.context.role;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.role.RoleUpdateEvent;

import java.util.Locale;

public class RoleUpdateContext extends DiscordBasePlatformContext<RoleUpdateEvent> {
    public RoleUpdateContext(Bot bot, DiscordPlatform platform, RoleUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public RoleUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, RoleUpdateEvent payload) {
        super(bot, platform, payload);
    }
}
