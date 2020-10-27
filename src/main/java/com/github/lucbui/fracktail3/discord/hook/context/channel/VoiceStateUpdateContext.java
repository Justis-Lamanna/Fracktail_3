package com.github.lucbui.fracktail3.discord.hook.context.channel;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.VoiceStateUpdateEvent;

import java.util.Locale;

public class VoiceStateUpdateContext extends DiscordBasePlatformContext<VoiceStateUpdateEvent> {
    public VoiceStateUpdateContext(Bot bot, DiscordPlatform platform, VoiceStateUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public VoiceStateUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, VoiceStateUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public boolean isJoin() {
        return !getPayload().getOld().isPresent();
    }

    public boolean isLeave() {
        return !getPayload().getCurrent().getChannelId().isPresent();
    }
}
