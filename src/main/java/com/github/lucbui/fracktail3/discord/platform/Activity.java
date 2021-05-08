package com.github.lucbui.fracktail3.discord.platform;

import discord4j.discordjson.json.ActivityUpdateRequest;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum Activity {
    NONE,
    PLAYING(discord4j.core.object.presence.Activity::playing),
    LISTENING(discord4j.core.object.presence.Activity::listening),
    WATCHING(discord4j.core.object.presence.Activity::watching),
    COMPETING(discord4j.core.object.presence.Activity::competing),
    STREAMING(discord4j.core.object.presence.Activity::streaming);

    private final BiFunction<String, String, ActivityUpdateRequest> weird;

    Activity() {
        this.weird = (one, two) -> {
            throw new IllegalArgumentException("Should not have been called");
        };
    }

    Activity(Function<String, ActivityUpdateRequest> normal) {
        this.weird = (one, unused) -> normal.apply(one);
    }

    Activity(BiFunction<String, String, ActivityUpdateRequest> weird) {
        this.weird = weird;
    }

    public ActivityUpdateRequest create(String status, String url) {
        return weird.apply(status, url);
    }
}
