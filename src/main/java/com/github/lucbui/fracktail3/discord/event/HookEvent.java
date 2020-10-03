package com.github.lucbui.fracktail3.discord.event;

/**
 * An event supported by the HookEvent interface
 * We use this, rather than the event directly, so we can implement interfaces on top.
 */
public interface HookEvent {
    /**
     * Get the type of event
     * @return The event being returned
     */
    DiscordSupportedEvent eventType();

    /**
     * Get the original event
     * @return The original event
     */
    Object getOriginalEvent();
}
