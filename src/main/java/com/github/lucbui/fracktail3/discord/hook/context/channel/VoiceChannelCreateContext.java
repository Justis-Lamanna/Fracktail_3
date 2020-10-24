package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.VoiceChannelCreateEvent;

import java.util.Locale;

public class VoiceChannelCreateContext extends DiscordBasePlatformContext<VoiceChannelCreateEvent> {
    public VoiceChannelCreateContext(Bot bot, DiscordPlatform platform, VoiceChannelCreateEvent payload) {
        super(bot, platform, payload);
    }

    public VoiceChannelCreateContext(Bot bot, DiscordPlatform platform, Locale locale, VoiceChannelCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
