package com.github.lucbui.fracktail3.discord.hook.context.lifecycle;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.lifecycle.ResumeEvent;

import java.util.Locale;

public class ResumeContext extends DiscordBasePlatformContext<ResumeEvent> {
    public ResumeContext(Bot bot, DiscordPlatform platform, ResumeEvent payload) {
        super(bot, platform, payload);
    }

    public ResumeContext(Bot bot, DiscordPlatform platform, Locale locale, ResumeEvent payload) {
        super(bot, platform, locale, payload);
    }
}
