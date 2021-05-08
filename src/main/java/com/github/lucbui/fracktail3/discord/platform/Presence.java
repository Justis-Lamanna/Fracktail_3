package com.github.lucbui.fracktail3.discord.platform;

import discord4j.discordjson.json.ActivityUpdateRequest;
import discord4j.discordjson.json.gateway.StatusUpdate;
import lombok.AllArgsConstructor;

import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
public enum Presence {
    ONLINE(discord4j.core.object.presence.Presence::online, discord4j.core.object.presence.Presence::online),
    IDLE(discord4j.core.object.presence.Presence::idle, discord4j.core.object.presence.Presence::idle),
    DO_NOT_DISTURB(discord4j.core.object.presence.Presence::doNotDisturb, discord4j.core.object.presence.Presence::doNotDisturb),
    INVISIBLE(discord4j.core.object.presence.Presence::invisible, null);

    private final Supplier<StatusUpdate> plain;
    private final Function<ActivityUpdateRequest, StatusUpdate> complex;

    public StatusUpdate create(Activity activity, String string, String url) {
        if(activity == Activity.NONE) {
            return plain.get();
        } else {
            return complex.apply(activity.create(string, url));
        }
    }
}
