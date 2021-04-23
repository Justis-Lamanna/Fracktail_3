package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import reactor.core.publisher.Mono;

public interface CommandUseContext {
    Bot getBot();
    Platform getPlatform();
    Command getCommand();
    Parameters getParameters();

    Object getTrigger();
    Person getSender();
    Mono<Place> getTriggerPlace();

    default Mono<Void> doAction() {
        return getCommand().doAction(this);
    }


}
