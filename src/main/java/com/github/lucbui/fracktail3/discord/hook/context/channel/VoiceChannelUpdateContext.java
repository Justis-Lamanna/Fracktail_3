package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.channel.VoiceChannelUpdateEvent;

import java.util.Locale;

public class VoiceChannelUpdateContext extends DiscordBasePlatformContext<VoiceChannelUpdateEvent> {
    public VoiceChannelUpdateContext(Bot bot, DiscordPlatform platform, VoiceChannelUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public VoiceChannelUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, VoiceChannelUpdateEvent payload) {
        super(bot, platform, payload);
    }
}
