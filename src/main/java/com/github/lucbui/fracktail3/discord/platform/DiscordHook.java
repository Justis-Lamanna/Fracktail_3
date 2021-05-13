package com.github.lucbui.fracktail3.discord.platform;

import discord4j.core.event.ReactiveEventAdapter;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DiscordHook {
    private final String id;
    private final ReactiveEventAdapter hook;

    public static List<DiscordHook> fromMap(Map<String, ReactiveEventAdapter> map) {
        return map.keySet()
                .stream()
                .map(id -> new DiscordHook(id, map.get(id)))
                .collect(Collectors.toList());
    }
}
