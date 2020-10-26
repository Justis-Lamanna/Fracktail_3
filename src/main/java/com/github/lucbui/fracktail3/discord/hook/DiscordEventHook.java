package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;

/**
 * A hook that activates when a certain type of event is emitted
 */
public class DiscordEventHook<T> implements Id, Disableable {
    private final String id;
    private final T hook;

    private boolean enabled;

    public DiscordEventHook(String id, T hook, boolean enabled) {
        this.id = id;
        this.hook = hook;
        this.enabled = enabled;
    }

    public DiscordEventHook(String id, T hook) {
        this(id, hook, true);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getId() {
        return id;
    }

    public T getHook() {
        return hook;
    }
}
