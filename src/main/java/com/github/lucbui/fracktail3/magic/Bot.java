package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.PlatformHandler;
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
        this.platformHandlers = new ArrayList<>(Arrays.asList(handlers));
    }

    public Bot addPlatformHandler(PlatformHandler handler) {
        platformHandlers.add(handler);
        return this;
    }

    public BotSpec getBotSpec() {
        return botSpec;
    }

    public Mono<Boolean> start() {
        if(CollectionUtils.isEmpty(platformHandlers)) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(platformHandlers)
                .flatMap(handler -> handler.start(botSpec))
                .last().thenReturn(true);
    }

    public Mono<Boolean> stop() {
        if(platformHandlers.isEmpty()) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(platformHandlers)
                .flatMap(handler -> handler.stop(botSpec))
                .last().thenReturn(true);
    }
}
