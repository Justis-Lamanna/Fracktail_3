package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.NonePerson;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.PlatformModel;
import com.github.lucbui.fracktail3.twitch.platform.TwitchPlatform;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.domain.UserList;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Data
@PlatformModel(TwitchPlatform.class)
public class TwitchBasicPerson implements Person, Formattable {
    private final TwitchClient client;
    private final EventUser eventUser;
    private final Optional<String> displayName;

    @Override
    public String getName() {
        return displayName.orElse(eventUser.getName());
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return Mono.just(new TwitchWhisperPlace(client, eventUser));
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? ("@" + getName()) : getName();
        formatter.format(output);
    }

    public Mono<Person> resolve() {
        return Mono.fromFuture(CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, Collections.singletonList(eventUser.getId()), null).execute()))
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(u -> new TwitchPerson(client, u))
                .cast(Person.class)
                .defaultIfEmpty(NonePerson.INSTANCE);
    }
}
