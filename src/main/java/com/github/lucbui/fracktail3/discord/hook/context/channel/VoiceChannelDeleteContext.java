package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.VoiceChannelDeleteEvent;

import java.util.Locale;

public class VoiceChannelDeleteContext extends DiscordBasePlatformContext<VoiceChannelDeleteEvent> {
    public VoiceChannelDeleteContext(Bot bot, DiscordPlatform platform, VoiceChannelDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public VoiceChannelDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, VoiceChannelDeleteEvent payload) {
        super(bot, platform, locale, payload);
    }
}
