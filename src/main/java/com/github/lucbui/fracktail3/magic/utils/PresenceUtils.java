package com.github.lucbui.fracktail3.magic.utils;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.xsd.DTDDiscordPresence;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;
import java.util.function.Supplier;

public class PresenceUtils {
    public static Presence getPresence(DTDDiscordPresence presence) {
        if(presence == null) {
            return Presence.online();
        }
        switch (presence.getStatus()) {
            case "ONLINE": return getPresence(presence, Presence::online, Presence::online);
            case "DO_NOT_DISTURB": return getPresence(presence, Presence::doNotDisturb, Presence::doNotDisturb);
            case "IDLE": return getPresence(presence, Presence::idle, Presence::idle);
            case "INVISIBLE": return Presence.invisible();
            default: throw new BotConfigurationException("Expected presence status: ONLINE, DO_NOT_DISTURB, IDLE, INVISIBLE (got " + presence.getStatus() + ")");
        }
    }

    private static Presence getPresence(DTDDiscordPresence presence, Function<Activity, Presence> optOne, Supplier<Presence> optTwo) {
        if(StringUtils.isNotBlank(presence.getListening())) {
            return optOne.apply(Activity.listening(presence.getListening()));
        }
        if(StringUtils.isNotBlank(presence.getPlaying())) {
            return optOne.apply(Activity.playing(presence.getPlaying()));
        }
        if(StringUtils.isNotBlank(presence.getWatching())) {
            return optOne.apply(Activity.watching(presence.getWatching()));
        }
        if(presence.getStreaming() != null && StringUtils.isNoneBlank(presence.getStreaming().getTitle(), presence.getStreaming().getUrl())) {
            return optOne.apply(Activity.streaming(presence.getStreaming().getTitle(), presence.getStreaming().getUrl()));
        }
        return optTwo.get();
    }
}
