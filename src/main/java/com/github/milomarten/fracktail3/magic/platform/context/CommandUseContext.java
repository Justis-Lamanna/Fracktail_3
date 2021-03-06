package com.github.milomarten.fracktail3.magic.platform.context;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.command.Command;
import com.github.milomarten.fracktail3.magic.guard.Guard;
import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.Place;
import com.github.milomarten.fracktail3.magic.platform.Platform;
import reactor.core.publisher.Mono;

import java.io.File;

public interface CommandUseContext {
    Bot getBot();
    Platform getPlatform();
    Command getCommand();
    Parameters getParameters();

    Object getTrigger();
    Person getSender();
    Mono<Place> getTriggerPlace();

    default Mono<Boolean> canDoAction() {
        Guard guard = getCommand().getRestriction();
        if(guard == null) return Mono.just(true);
        return guard.matches(this);
    }

    default Mono<Void> doAction() {
        return getCommand().doAction(this);
    }

    default Mono<Void> respond(String message, File... files) {
        return getTriggerPlace()
                .flatMap(place -> place.sendMessage(message, files))
                .then();
    }
}
