package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.VoiceServerUpdateEvent;

import java.util.Locale;

public class VoiceServerUpdateContext extends DiscordBasePlatformContext<VoiceServerUpdateEvent> {
    public VoiceServerUpdateContext(Bot bot, DiscordPlatform platform, VoiceServerUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public VoiceServerUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, VoiceServerUpdateEvent payload) {
        super(bot, platform, payload);
    }
}
