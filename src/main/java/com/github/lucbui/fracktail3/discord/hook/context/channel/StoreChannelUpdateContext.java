package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.StoreChannelUpdateEvent;

import java.util.Locale;

public class StoreChannelUpdateContext extends DiscordBasePlatformContext<StoreChannelUpdateEvent> {
    public StoreChannelUpdateContext(Bot bot, DiscordPlatform platform, StoreChannelUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public StoreChannelUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, StoreChannelUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
