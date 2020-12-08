package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.BotResponse;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * A collection of standard return conversion function
 */
public class StdReturnConverterFunctions {
    /**
     * ReturnConverterFunction which handles a void or Void return.
     * This simply does nothing with the return, since there is no return
     */
    public static class Voids implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return Mono.empty();
        }
    }

    /**
     * ReturnConverterFunction which handles a Mono return.
     * The returned Mono completes when the input mono completes.
     * If null, this returns an empty Mono
     */
    public static class Monos implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : ((Mono<?>)o).then();
        }
    }

    /**
     * ReturnConverterFunction which handles a Flux return.
     * The returned Mono completes when the input flux completes.
     * If null, this returns an empty Mono
     */
    public static class Fluxs implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : ((Flux<?>)o).then();
        }
    }

    /**
     * ReturnConverterFunction which handles a String return.
     * The returned string is sent to the command user as a response.
     * If null, this returns an empty Mono and does nothing.
     */
    public static class Strings implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : context.respond((String)o);
        }
    }

    /**
     * ReturnConverterFunction which handles a FormattedString return.
     * The returned FormattedString is resolved and sent to the command user as a response.
     * If null, this returns an empty Mono and does nothing.
     */
    public static class FStrings implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : context.respond((FormattedString) o, Collections.emptyMap());
        }
    }

    /**
     * ReturnConverterFunction which handles a BotResponse return.
     * The returned BotResponse is resolved and sent to the command user as a response.
     * If null, this returns an empty Mono and does nothing.
     */
    public static class BotResponses implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : context.respond(((BotResponse)o).respondWith(), Collections.emptyMap());
        }
    }
}
