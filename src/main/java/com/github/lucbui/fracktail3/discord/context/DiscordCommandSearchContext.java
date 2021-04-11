package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Basic context for MessageCreateEvent
 */
public class DiscordCommandSearchContext extends DiscordBasePlatformContext<MessageCreateEvent> {
    private Mono<Locale> cached;

    /**
     * Initialize
     * @param bot The bot
     * @param platform The platform
     * @param payload The payload
     */
    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
        cached = payload.getGuild()
                .map(Guild::getPreferredLocale)
                .defaultIfEmpty(Locale.getDefault())
                .cache();
    }

    /**
     * Initialize with Locale
     * @param bot The bot
     * @param platform The platform
     * @param locale The locale
     * @param payload The payload
     */
    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    @Override
    public Mono<Place> getTriggerPlace() {
        return getPayload().getMessage().getChannel().map(DiscordPlace::new);
    }

    @Override
    public Mono<Locale> getLocale() {
        return cached;
    }
}
