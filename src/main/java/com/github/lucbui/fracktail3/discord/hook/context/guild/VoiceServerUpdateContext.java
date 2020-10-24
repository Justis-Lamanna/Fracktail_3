package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.discordjson.json.gateway.VoiceServerUpdate;

import java.util.Locale;

public class VoiceServerUpdateContext extends DiscordBasePlatformContext<VoiceServerUpdate> {
    public VoiceServerUpdateContext(Bot bot, DiscordPlatform platform, VoiceServerUpdate payload) {
        super(bot, platform, payload);
    }

    public VoiceServerUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, VoiceServerUpdate payload) {
        super(bot, platform, locale, payload);
    }
}
