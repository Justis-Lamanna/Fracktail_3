package com.github.milomarten.fracktail3.twitch.platform;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TwitchHook {
    private final String id;
    private final TwitchEventAdapter hook;

    public static List<TwitchHook> fromMap(Map<String, TwitchEventAdapter> map) {
        return map.keySet().stream()
                .map(id -> new TwitchHook(id, map.get(id)))
                .collect(Collectors.toList());
    }
}
