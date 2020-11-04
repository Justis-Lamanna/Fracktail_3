package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.magic.util.IdStore;
import org.apache.commons.lang3.ClassUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscordEventHookStore2 extends IdStore<DiscordEventHook<?>> {
    public DiscordEventHookStore2() {
        this(Collections.emptyMap());
    }

    public DiscordEventHookStore2(Map<String, DiscordEventHook<?>> store) {
        super(store);
    }

    public DiscordEventHookStore2(List<DiscordEventHook<?>> discordEventHooks) {
        super(discordEventHooks);
    }

    public <T> List<DiscordEventHook<? extends T>> getAllFor(Class<T> clazz) {
        return getAll().stream()
                .filter(hook -> ClassUtils.isAssignable(hook.getHook().getClass(), clazz))
                .map(hook -> (DiscordEventHook<? extends T>)hook)
                .collect(Collectors.toList());
    }
}
