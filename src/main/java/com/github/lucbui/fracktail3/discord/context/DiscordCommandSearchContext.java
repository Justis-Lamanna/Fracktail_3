package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.guard.DiscordUserset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class DiscordCommandSearchContext extends DiscordBasePlatformContext<MessageCreateEvent> {
    private Mono<Locale> cached;

    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
        cached = payload.getGuild()
                .map(Guild::getPreferredLocale)
                .defaultIfEmpty(Locale.getDefault())
                .cache();
    }

    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    @Override
    public Mono<Void> respond(String message) {
        return getPlatform()
                .message(DiscordChannelset.forChannel(getPayload().getMessage().getChannelId()), message);
    }

    @Override
    public Mono<Void> privateMessage(String message) {
        return getPayload().getMessage().getAuthor()
                .map(user -> getPlatform().dm(DiscordUserset.forUser(user.getId()), message))
                .orElse(Mono.empty());
    }

    @Override
    public Mono<Locale> getLocale() {
        return cached;
    }
}
