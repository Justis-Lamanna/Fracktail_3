package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

public class SimpleTextCommandProcessor {
    public static Disposable listenForCommands(Mono<Place> commandPlace, Bot bot, Platform platform, String prefix, ContextConstructor constructor) {
        return commandPlace.flatMapMany(Place::getMessageFeed)
                .filter(m -> m.getSender() != NonePerson.INSTANCE)
                .filter(m -> !m.getSender().isBot())
                .filter(message -> message.getContent().startsWith(prefix))
                .flatMap(message -> {
                    return Flux.fromIterable(bot.getSpec().getCommandList().getCommands())
                            .flatMap(c -> Flux.fromIterable(c.getNames()).map(name -> Tuples.of(name, c)))
                            .filter(t -> message.getContent().startsWith(prefix + t.getT1()))
                            .map(t -> {
                                String cmdStr = t.getT1();
                                String pStr = StringUtils.removeStart(message.getContent(), prefix + cmdStr)
                                        .trim();
                                return constructor.constructContext(bot, platform, message, t.getT2(), pStr);
                            })
                            .filterWhen(CommandUseContext::canDoAction)
                            .next()
                            .flatMap(CommandUseContext::doAction)
                            .onErrorResume(Throwable.class, e -> {
                                LoggerFactory.getLogger(bot.getClass()).error("Error during action", e);
                                return message.getOrigin()
                                        .flatMap(place ->
                                                place.sendMessage("I ran into an error there, sorry: " + e.getMessage() + ". Check the logs for more info."))
                                        .then();
                            });
                })
                .subscribe();
    }
}
