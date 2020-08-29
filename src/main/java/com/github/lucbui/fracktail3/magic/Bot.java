package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.platform.PlatformHandler;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot {
    private final BotSpec botSpec;
    private final List<PlatformHandler> platformHandlers;

    public Bot(BotSpec botSpec, PlatformHandler... handlers) {
        this.botSpec = botSpec;
        this.platformHandlers = handlers.length == 0 ?
                new ArrayList<>() :
                new ArrayList<>(Arrays.asList(handlers));
    }

    public Bot addPlatformHandler(PlatformHandler handler) {
        platformHandlers.add(handler);
        return this;
    }

    public BotSpec getSpec() {
        return botSpec;
    }

    public Mono<Boolean> start() {
        if(CollectionUtils.isEmpty(platformHandlers)) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(platformHandlers)
                .flatMap(handler -> handler.start(this))
                .last(true);
    }

    public Mono<Boolean> stop() {
        if(platformHandlers.isEmpty()) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(platformHandlers)
                .flatMap(handler -> handler.stop(this))
                .last(true);
    }
}
