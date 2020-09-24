package com.github.lucbui.fracktail3.magic.platform.discord;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Service which extracts a locale from a Discord event
 * (Allows for swapping between different methods of resolving, or forcing certain locales)
 */
public interface DiscordLocaleResolver {
    /**
     * Extract the locale from the event
     * @param event The event to extract from
     * @return Asynchronously-determined Locale
     */
    Mono<Locale> getLocale(MessageCreateEvent event);

    /**
     * DiscordLocaleResolver which just returns the same locale
     * @param locale The locale to use
     * @return DiscordLocaleResolver which just returns locale regardless
     */
    static DiscordLocaleResolver identity(Locale locale) {
        return event -> Mono.just(locale);
    }
}
