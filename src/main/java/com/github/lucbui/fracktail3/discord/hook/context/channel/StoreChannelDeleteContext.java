package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.StoreChannelDeleteEvent;

import java.util.Locale;

public class StoreChannelDeleteContext extends DiscordBasePlatformContext<StoreChannelDeleteEvent> {
    public StoreChannelDeleteContext(Bot bot, DiscordPlatform platform, StoreChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public StoreChannelDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, StoreChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }
}
