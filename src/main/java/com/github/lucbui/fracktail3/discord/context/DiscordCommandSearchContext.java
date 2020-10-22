package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.context.RespondingContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class DiscordCommandSearchContext extends DiscordBaseContext<MessageCreateEvent> implements RespondingContext {
    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, locale, payload);
    }

    @Override
    public Mono<Void> respond(String message) {
        return getPlatform().message(DiscordChannelset.forChannel("", getPayload().getMessage().getChannelId()), message);
    }
}
