package com.github.lucbui.fracktail3.discord.platform;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Resolver which extracts the locale from the message's guild
 */
public class LocaleFromGuildResolver implements DiscordLocaleResolver {
    private final Locale defaultLocale;

    /**
     * Initialize this Resolver
     * @param defaultLocale The locale to use, if none is specified in the Guild, or is a DM
     */
    public LocaleFromGuildResolver(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * Initialize this Resolver
     * If the guild has no locale, or it is a DM, the system's default locale is used
     */
    public LocaleFromGuildResolver() {
        this.defaultLocale = Locale.getDefault();
    }

    @Override
    public Mono<Locale> getLocale(MessageCreateEvent event) {
        return event.getGuild().map(Guild::getPreferredLocale).defaultIfEmpty(defaultLocale);
    }
}
