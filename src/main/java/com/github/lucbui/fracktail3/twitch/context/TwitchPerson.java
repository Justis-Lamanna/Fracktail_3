package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.PlatformModel;
import com.github.lucbui.fracktail3.twitch.platform.TwitchPlatform;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.domain.User;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;

@Data
@PlatformModel(TwitchPlatform.class)
public class TwitchPerson implements Person, Formattable {
    private final TwitchClient client;
    private final User user;

    @Override
    public String getName() {
        return user.getDisplayName();
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return Mono.just(new TwitchWhisperPlace(client, new EventUser(user.getId(), user.getDisplayName())));
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? ("@" + user.getDisplayName()) : getName();
        formatter.format(output);
    }
}
