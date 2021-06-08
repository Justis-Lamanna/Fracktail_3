package com.github.milomarten.fracktail3.magic.platform.context;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.command.Command;
import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.Place;
import com.github.milomarten.fracktail3.magic.platform.Platform;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class BasicCommandUseContext implements CommandUseContext {
    private final Bot bot;
    private final Platform platform;
    private final Object trigger;
    private final Person sender;
    private final Mono<Place> triggerPlace;
    private final Command command;
    private final Parameters parameters;
}
