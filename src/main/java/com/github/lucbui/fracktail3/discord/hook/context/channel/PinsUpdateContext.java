package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;

import java.util.Locale;

public class PinsUpdateContext extends DiscordBasePlatformContext<PinsUpdateContext> {
    public PinsUpdateContext(Bot bot, DiscordPlatform platform, PinsUpdateContext payload) {
        super(bot, platform, payload);
    }

    public PinsUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, PinsUpdateContext payload) {
        super(bot, platform, locale, payload);
    }
}
