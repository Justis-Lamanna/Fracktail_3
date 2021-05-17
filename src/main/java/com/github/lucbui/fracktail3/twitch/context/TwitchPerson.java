package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.Optional;

@Data
public class TwitchPerson implements Person, Formattable {
    private final TwitchClient client;
    private final EventUser eventUser;
    private final Optional<String> displayName;

    @Override
    public String getName() {
        return displayName.orElse(eventUser.getName());
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return Mono.just(new TwitchWhisperPlace(client, eventUser.getName()));
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? ("@" + eventUser.getName()) : getName();
        formatter.format(output);
    }
}
