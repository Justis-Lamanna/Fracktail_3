package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.StoreChannelCreateEvent;

import java.util.Locale;

public class StoreChannelCreateContext extends DiscordBasePlatformContext<StoreChannelCreateEvent> {
    public StoreChannelCreateContext(Bot bot, DiscordPlatform platform, StoreChannelCreateEvent payload) {
        super(bot, platform, payload);
    }

    public StoreChannelCreateContext(Bot bot, DiscordPlatform platform, Locale locale, StoreChannelCreateEvent payload) {
        super(bot, platform, payload);
    }
}
