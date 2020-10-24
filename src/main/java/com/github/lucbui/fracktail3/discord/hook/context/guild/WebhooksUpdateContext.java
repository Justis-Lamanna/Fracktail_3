package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.WebhooksUpdateEvent;

import java.util.Locale;

public class WebhooksUpdateContext extends DiscordBasePlatformContext<WebhooksUpdateEvent> {
    public WebhooksUpdateContext(Bot bot, DiscordPlatform platform, WebhooksUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public WebhooksUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, WebhooksUpdateEvent payload) {
        super(bot, platform, locale, payload);
    }
}
