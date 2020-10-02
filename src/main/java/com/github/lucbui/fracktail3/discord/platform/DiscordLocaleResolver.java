package com.github.lucbui.fracktail3.discord.platform;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Service which extracts a locale from a Discord event
 * (Allows for swapping between different methods of resolving, or forcing certain locales)
 * @param <E> The type of event
 */
public interface DiscordLocaleResolver<E extends Event> {
    /**
     * Extract the locale from the event
     * @param event The event to extract from
     * @return Asynchronously-determined Locale
     */
    Mono<Locale> getLocale(E event);

    /**
     * DiscordLocaleResolver which just returns the same locale
     * @param locale The locale to use
     * @return DiscordLocaleResolver which just returns locale regardless
     */
    static <E extends Event> DiscordLocaleResolver<E> identity(Locale locale) {
        return event -> Mono.just(locale);
    }
}
