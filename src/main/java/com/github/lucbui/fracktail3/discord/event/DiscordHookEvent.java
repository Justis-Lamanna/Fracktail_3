package com.github.lucbui.fracktail3.discord.event;

import com.github.lucbui.fracktail3.magic.hook.HookEvent;

/**
 * An event supported by the HookEvent interface
 * We use this, rather than the event directly, so we can implement interfaces on top.
 */
public interface DiscordHookEvent<EVENT> extends HookEvent<DiscordSupportedEvent, EVENT> { }
