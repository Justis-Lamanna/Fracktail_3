package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.magic.util.IBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Store of all Event Hooks.
 */
public class DiscordEventHookStoreBuilder2 implements IBuilder<DiscordEventHookStore2> {
    private final List<DiscordEventHook<?>> hooks = new ArrayList<>();

    public DiscordEventHookStoreBuilder2 withHook(DiscordEventHook<?> hook) {
        this.hooks.add(hook);
        return this;
    }

    @Override
    public DiscordEventHookStore2 build() {
        return new DiscordEventHookStore2(hooks);
    }
}
