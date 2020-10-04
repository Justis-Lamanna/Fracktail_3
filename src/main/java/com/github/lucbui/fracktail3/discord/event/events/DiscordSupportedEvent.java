package com.github.lucbui.fracktail3.discord.event.events;

import com.github.lucbui.fracktail3.magic.hook.HookEvent;
import com.github.lucbui.fracktail3.magic.hook.SupportedEvent;
import discord4j.core.event.domain.Event;
import org.apache.commons.lang3.ClassUtils;

import java.util.Optional;

/**
 * A marking class which allows for convenient conversion from Discord Events to custom HookEvents.
 * NORMALLY we'd use an enum, but this approach allows more convenience while defining the hooks themselves (no casting
 * is necessary)
 * @param <E> The event type
 * @param <HE> The HookEvent type
 */
public abstract class DiscordSupportedEvent<
        E extends Event,
        HE extends HookEvent<E>> implements SupportedEvent {

    /**
     * The basic converter from Discord Event to Hook Event
     * @param clazz The class representing the desired Event
     * @param <E> The type of the desired Event
     * @return A DiscordSupportedEvent which matches on events of the supplied class
     */
    public static <E extends Event> DiscordSupportedEvent<E, WrappedDiscordHookEvent<E>> forEvent(Class<E> clazz) {
        return new Default<>(clazz);
    }

    /**
     * If the provided event is supported, map to the HookEvent type
     * @param event The event to convert
     * @return An Optional converting the provided event, or empty if unsupported
     */
    public abstract Optional<HE> convertIfSupported(Event event);

    private static class Default<E extends Event> extends DiscordSupportedEvent<E, WrappedDiscordHookEvent<E>> {
        private final Class<E> clazz;

        public Default(Class<E> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Optional<WrappedDiscordHookEvent<E>> convertIfSupported(Event event) {
            if(ClassUtils.isAssignable(event.getClass(), clazz)) {
                return Optional.of(new WrappedDiscordHookEvent<>(clazz.cast(event)));
            }
            return Optional.empty();
        }
    }
}
