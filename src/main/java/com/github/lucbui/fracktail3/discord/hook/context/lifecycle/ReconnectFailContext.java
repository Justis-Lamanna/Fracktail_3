package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;

import java.util.Locale;

public class ReconnectFailContext extends DiscordBasePlatformContext<ReconnectFailContext> {
    public ReconnectFailContext(Bot bot, DiscordPlatform platform, ReconnectFailContext payload) {
        super(bot, platform, payload);
    }

    public ReconnectFailContext(Bot bot, DiscordPlatform platform, Locale locale, ReconnectFailContext payload) {
        super(bot, platform, locale, payload);
    }
}
