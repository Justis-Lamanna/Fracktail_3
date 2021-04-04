package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class DiscordPerson implements Person {
    private final discord4j.core.object.entity.User user;

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return user.getPrivateChannel()
                .map(DiscordPlace::new);
    }
}
