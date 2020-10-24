package com.github.lucbui.fracktail3.discord.hook.context.user;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.UserUpdateEvent;

import java.util.Locale;

public class UserUpdateContext extends DiscordBasePlatformContext<UserUpdateEvent> {
    public UserUpdateContext(Bot bot, DiscordPlatform platform, UserUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public UserUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, UserUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
