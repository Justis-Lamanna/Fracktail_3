package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.MemberUpdateEvent;

import java.util.Locale;

public class MemberUpdateContext extends DiscordBasePlatformContext<MemberUpdateEvent> {
    public MemberUpdateContext(Bot bot, DiscordPlatform platform, MemberUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public MemberUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, MemberUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
